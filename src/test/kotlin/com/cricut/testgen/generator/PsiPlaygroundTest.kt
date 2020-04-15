package com.cricut.testgen.generator

import com.intellij.lang.Language
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFileFactory
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixture4TestCase
import junit.framework.TestCase
import org.jetbrains.kotlin.idea.KotlinFileType
import org.jetbrains.kotlin.idea.KotlinLanguage
import org.jetbrains.kotlin.psi.KtElement
import org.jetbrains.kotlin.psi.KtFile
import org.junit.Test
import kotlin.reflect.jvm.internal.impl.load.java.structure.JavaElement


class PsiPlaygroundTest: LightPlatformCodeInsightFixture4TestCase() {

    override fun getTestDataPath(): String {
        return "src/test/testData"
    }

    @Test
    fun readJavaFile(){
        val file = LocalFileSystem.getInstance().findFileByPath("src/test/testData/CompleteTestData.java")
        println("This is my virtual java file: $file")

        TestCase.assertTrue(file != null)
        val psiFile = psiManager.findFile(file!!)!!
        println("This is a psi file: $psiFile")
        println("PSI File Type: ${psiFile.fileType}")
        println("PSI File viewProvider: ${psiFile.viewProvider}")
        println("PSI File children: ${psiFile.children.size}")
        println("PSI File child text: ${psiFile.children.map { it.text }}")
    }

    @Test
    fun readKotlinFile(){
        val kotlinFile = LocalFileSystem.getInstance().findFileByPath("src/test/testData/Test.kt")
        println("This is my virtual kotlin file: $kotlinFile")

        TestCase.assertTrue(kotlinFile != null)
        val kotlinPsi = psiManager.findFile(kotlinFile!!)
        println("This is a psi kotlin file: $kotlinPsi")
    }

    @Test
    fun writeKotlinFile(){
        val newFile = PsiFileFactory.getInstance(project).createFileFromText(
            "NewKotlinFile.kt",KotlinFileType.INSTANCE as FileType,"data class Thing(val neat: String)"
        )

        println("This is a psi kotlin file: $newFile")
    }

    @Suppress("CAST_NEVER_SUCCEEDS")
    @Test
    fun writeKotlinFileByLanguage(){
        val newFile = PsiFileFactory.getInstance(project).createFileFromText(
            "NewFile.kt", KotlinLanguage.INSTANCE as Language, "data class Noob(val level: Int)"
        )

        println("This is a psi kotlin file: $newFile")
    }

    @Test
    fun `what is in aData class ?`(){
        val dataClass = PsiFileFactory.getInstance(project).createFileFromText(
            "NewFile.kt", KotlinLanguage.INSTANCE as Language, "data class Noob(val level: Int, val thing: String)"
        ) as KtFile

        whatIsInAFile(dataClass)
    }

    @Test
    fun `what is in regular class ?`(){
        val regularClass = PsiFileFactory.getInstance(project).createFileFromText(
            "NewFile.kt", KotlinLanguage.INSTANCE as Language, "class Noob {\n\tfun doSomething()\n\n\tfun doSomethingElse(int: Int): Boolean { return false }\n}"
        ) as KtFile

        whatIsInAFile(regularClass)
    }

    fun whatIsInAFile(ktFile: KtFile){
        println("This is a psi kt file: $ktFile")

        println("This is the import list: ${ktFile.importList}")
        println("This is the import directives: ${ktFile.importDirectives}")


        ktFile.children.map {
            (it as PsiElement).followPSITree(0) { element, level ->
                val spaces = (0..level).map { " " }.toString()
                when (element) {
                    is KtElement -> {
                        println("$spaces Kotlin Element: $element")

            //                    val ktElement = element as KtElement
            //                    println("Kotlin Element Details: ${ktElement.isPhysical} ${ktElement.isValid} ${ktElement.isWritable} ${ktElement.isFakeElement} ${ktElement.text}")

                    }
                    is JavaElement -> {
                        println("$spaces Java Element: $element")
                    }
                    else -> {
                        println("$spaces Unknown Element: $element")
                    }
                }
            }
        }

        ktFile.classes.map {
            println("Class: $it")

        }

        println("This is the classes: ${ktFile.classes}")

        println("This is the annotaions list: ${ktFile.fileAnnotationList}")
    }


}
