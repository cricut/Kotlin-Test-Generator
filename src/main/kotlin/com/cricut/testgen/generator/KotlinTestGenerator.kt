package com.cricut.testgen.generator

import com.intellij.psi.PsiElement
import com.intellij.psi.codeStyle.CodeStyleManager
import org.jetbrains.kotlin.idea.imports.KotlinImportOptimizer
import org.jetbrains.kotlin.idea.quickfix.createFromUsage.callableBuilder.getReturnTypeReference
import org.jetbrains.kotlin.lexer.KtModifierKeywordToken
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.psi.psiUtil.getPrevSiblingIgnoringWhitespace
import org.jetbrains.kotlin.resolve.ImportPath

class KotlinTestGenerator {
    data class TestSettings(
        val callFunctions: Boolean = false,
        val mockDependencies: Boolean = false,
        val makeFailingTests: Boolean = false
    )

    fun generateTest(oldFile: KtFile, settings: TestSettings = TestSettings()): KtFile {
        val newFile = oldFile.copy() as KtFile
        val factory = KtPsiFactory(newFile.project, false)

        val ktClass = getClass(newFile, factory) ?: return newFile
        val oldName = ktClass.name
        val newClassName = KtFileEditor.renameClass(ktClass, oldName)
        KtFileEditor.renameFile(newFile, newClassName)

        KtFileEditor.removeClassAnnotation(ktClass)
        KtFileEditor.removeInitFun(ktClass)
        KtFileEditor.removeProperties(ktClass)
        KtFileEditor.removeNestedClasses(ktClass)
        KtFileEditor.removeCompanionObjects(ktClass)

        val functions =
            KtFileEditor.getFunctionsAndDeletePrivateAndProtectedOnes(
                ktClass
            )
        val primaryConstructor = ktClass.primaryConstructor

        KtFileEditor.removeConstructors(ktClass)
        KtFileEditor.removeSuperTypeList(ktClass)

        val listOfArgs: List<String> = getFunctionArgumentDefaults(functions, settings)
        val listOfReturnTypes: List<String> = getFunctionReturnTypes(functions)
        cleanFunctions(factory, functions)
        annotateMethods(factory, functions)

        if(settings.callFunctions){
            val classDetails = ClassDetails(
                ktClass,
                oldName,
                primaryConstructor,
                functions,
                listOfArgs,
                listOfReturnTypes
            )
            makeTestsForClassFunctions(factory, classDetails, settings)
        }

        reformat(newFile)

        return newFile
    }

    private fun reformat(newFile: KtFile) {
        val codeStyleManager = CodeStyleManager.getInstance(newFile.project)
        codeStyleManager.reformat(newFile)
    }

    data class ClassDetails(val ktClass: KtClassOrObject,
                            val oldName: String?,
                            val primaryConstructor: KtPrimaryConstructor?,
                            val functions: List<KtFunction>,
                            val listOfArgs: List<String>,
                            val listOfReturnTypes: List<String>)

    private fun makeTestsForClassFunctions(
        factory: KtPsiFactory,
        classDetails: ClassDetails,
        testSettings: TestSettings
    ){
        val body = classDetails.ktClass.body
        if(body != null && classDetails.oldName != null) {
            if(testSettings.mockDependencies) {
                createObjectUnderTestWithMocks(factory, body, classDetails.oldName, classDetails)
            } else {
                createObjectUnderTest(factory, body, classDetails.oldName, classDetails)
            }
            stubFunctions(factory, classDetails, testSettings.makeFailingTests)
        }
    }

    private fun createObjectUnderTestWithMocks(
        factory: KtPsiFactory,
        body: KtClassBody,
        objectToTestName: String,
        classDetails: ClassDetails
    ) {
        val thing = classDetails.primaryConstructor?.valueParameterList
        val params = thing?.parameters
        val paramTypeReferences = params?.mapNotNull { it.typeReference } ?: emptyList()

        val mockkAnnotationEntry = factory.createAnnotationEntry("@io.mockk.impl.annotations.MockK")
        val lateinitModifier = KtModifierKeywordToken.keywordModifier("lateinit")

        val mockkedDependencies: List<KtProperty> = paramTypeReferences.map {
            val name = if(it.typeElement is KtUserType) {
                (it.typeElement as KtUserType).referencedName?.decapitalize() ?: it.text.decapitalize()
            } else {
                if(it.typeElement is KtFunctionType) "function"
                else it.text.decapitalize()
            }
            val property = factory.createProperty(
                name = name,
                type = it.text,
                isVar = true
            )
            property.addModifier(lateinitModifier)
            property.addAnnotationEntry(mockkAnnotationEntry)
            property
        }

        val dependencyList = StringBuilder()
        paramTypeReferences
            .map {
                if(it.typeElement is KtUserType) {
                    (it.typeElement as KtUserType).referencedName?.decapitalize() ?: it.text.decapitalize()
                } else {
                    it.text.decapitalize()
                }
            }
            .forEach { dependencyList.append("${it}, ") }

        val initializer: String = if(classDetails.ktClass is KtClass) "$objectToTestName(${dependencyList.dropLast(2)})" else objectToTestName

        val property = factory.createProperty(
            name = objectToTestName.decapitalize(),
            type = objectToTestName,
            isVar = true
        )

        property.addModifier(lateinitModifier)

        val setupFunction = factory.createFunction("" +
                "    fun setup(){\n" +
                "        io.mockk.MockKAnnotations.init(this, relaxed = true)\n" +
                "        ${objectToTestName.decapitalize()} = $initializer\n" +
                "    }")

        val annotation = factory.createAnnotationEntry("@org.junit.Before")
        setupFunction.addAnnotationEntry(annotation)

        body.addAfter(setupFunction, body.lBrace)
        body.addAfter(property, body.lBrace)
        mockkedDependencies.forEach { body.addAfter(it, body.lBrace) }
    }

    private fun getFunctionArgumentDefaults(
        functions: List<KtFunction>,
        settings: TestSettings
    ): List<String> {
        return functions.map {function ->
            val parameterList = function.valueParameterList
            parameterList?.let { list ->
                val params = list.parameters.map { parameter ->
                    getArgByParameter(parameter, settings)
                }
                val stringBuilder = StringBuilder()
                params.forEach { stringBuilder.append("$it, ") }
                return@map stringBuilder.toString().dropLast(2)
            }
            return@map ""
        }

    }

    private fun getArgByParameter(parameter: KtParameter?, settings: TestSettings): String {
        if(parameter != null){
            val typeReference = parameter.typeReference
            val typeElement = typeReference?.typeElement
            val innerTypeText = if(typeElement is KtNullableType) typeElement.innerType?.text else typeElement?.text
            if(innerTypeText != null) {
                when (innerTypeText) {
                    "Int" -> return "-1"
                    "Short" -> return  "-1"
                    "Byte" -> return  "-1"
                    "Long" -> return  "-1L"
                    "Float" -> return  "-1f"
                    "Double" -> return  "-1.0"
                    "Boolean" -> return  "true"
                    "Unit" -> return  "Unit"
                    "Char" -> return  "\'a\'"
                    "String" -> return  "\"Dope\""
                }

            }
        }
        return if(settings.mockDependencies) "io.mockk.mockk(relaxed = true)" else ""
    }

    private fun getFunctionReturnTypes(functions: List<KtFunction>): List<String> {
        return functions.map {
            it.getReturnTypeReference()?.text ?: ""
        }
    }

    private fun createObjectUnderTest(factory: KtPsiFactory, body: KtClassBody, objectToTestName: String, classDetails: ClassDetails) {

        val thing = classDetails.primaryConstructor?.valueParameterList
        val params = thing?.parameters
        val paramTypeReferences = params?.map { it.typeReference?.text }
        val dependencyList = StringBuilder()
        paramTypeReferences?.forEach { dependencyList.append("$it()") }
        val initializer: String = if(classDetails.ktClass is KtClass) "$objectToTestName($dependencyList)" else objectToTestName

        body.addAfter(
            factory.createProperty(
                name = objectToTestName.decapitalize(),
                type = objectToTestName,
                isVar = false,
                initializer = initializer
            ), body.lBrace
        )
    }

    private fun stubFunctions(
        factory: KtPsiFactory,
        classDetails: ClassDetails,
        makeFailingTests: Boolean
    ){
        classDetails.apply {
            functions.forEachIndexed { index, function ->
                val returnType = listOfReturnTypes.get(index)
                val functionCall = "${oldName?.decapitalize()}.${function.name}(${listOfArgs[index]})"
                val bodyText = if(returnType.isEmpty() || returnType == "Unit") {
                    functionCall
                } else {
                    val defaultValue = getDefaultValue(returnType)
                    val assert = if(returnType == "Double") {
                        "org.junit.Assert.assertEquals($defaultValue, value, 0.01)"
                    } else {
                        "org.junit.Assert.assertEquals($defaultValue, value)"
                    }
                    "val value = $functionCall\n$assert"
                }
                val failingTests = if(makeFailingTests) "\norg.junit.Assert.assertTrue(false)" else ""

                if(function.hasBlockBody()) {
                    function.bodyBlockExpression?.replace(factory.createBlock(bodyText + failingTests))
                } else {
                    function.bodyExpression?.getPrevSiblingIgnoringWhitespace()?.delete() // Problem line - com.intellij.psi.PsiInvalidElementAccessException: Element: class com.intellij.psi.impl.source.tree.LeafPsiElement #kotlin  because: different providers: com.intellij.psi.DummyHolderViewProvider{myVirtualFile=LightVirtualFile: /DummyHolder, content=VirtualFileContent{size=0}}(7d608003); com.intellij.psi.SingleRootFileViewProvider{myVirtualFile=LightVirtualFile: /DummyHolder, content=VirtualFileContent{size=0}}(6dd7ac30)
                    function.bodyExpression?.replace(factory.createBlock(bodyText + failingTests))
                }

            }
        }
    }

    private fun getDefaultValue(returnType: String): String {
        if(returnType.lastOrNull() == '?') return getDefaultValue(returnType.dropLast(1))
        return when (returnType) {
            "Boolean" -> "true"
            "Short" -> "(-1).toShort()"
            "Int" -> "-1"
            "Long" -> "-1L"
            "Float" -> "-1f"
            "Double" -> "-1.0"
            "Char" -> "\'a\'"
            "String" -> "\"Dope\""
            "Byte" -> "(-1).toByte()"
            else -> "null"
        }
    }

    private fun cleanFunctions(factory: KtPsiFactory, functions: List<KtFunction>){
        functions.forEach { function ->
            val body = function.bodyBlockExpression
            body?.replace(factory.createEmptyBody()) // Problem line - com.intellij.psi.PsiInvalidElementAccessException: Element: class org.jetbrains.kotlin.psi.KtBlockExpression #kotlin  because: containing file is null
            function.valueParameterList?.replace(factory.createParameterList("()"))
            function.typeReference?.delete()
            function.colon?.delete()
            function.modifierList?.delete()
        }
    }

    private fun annotateMethods(factory: KtPsiFactory, functions: List<KtFunction>){
        val annotation = factory.createAnnotationEntry("@org.junit.Test")
        functions.forEach { function ->
            function.addAnnotationEntry(annotation)
            function.addAfter(factory.createWhiteSpace("\n\t"), function.modifierList)
        }
    }

    private fun getClass(ktFile: KtFile, factory: KtPsiFactory): KtClassOrObject? {
        val classes: ArrayList<KtClassOrObject> = ArrayList()
        ktFile.children.forEach { element ->
            (element as PsiElement).followPSITree(0) { psiElement, _ ->
                if(psiElement is KtClassOrObject) {
                    psiElement.getDeclarationKeyword()?.replace(factory.createClassKeyword())
                    classes.add(psiElement)
                }
            }
        }
        return classes.firstOrNull()
    }

    fun optimizeImport(newFile: KtFile): KtFile {
        KotlinImportOptimizer.replaceImports(newFile, listOf(
            ImportPath(FqName("org.junit.Test"), false)
        ))

        val codeStyleManager = CodeStyleManager.getInstance(newFile.project)
        codeStyleManager.reformat(newFile)

        return newFile
    }
}