package com.cricut.testgen.generator

import com.cricut.testgen.generator.KotlinTestGenerator
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.psi.PsiFileFactory
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixture4TestCase
import junit.framework.TestCase
import org.jetbrains.kotlin.idea.KotlinFileType
import org.jetbrains.kotlin.psi.*
import org.junit.Test


class KotlinGeneration: LightPlatformCodeInsightFixture4TestCase() {

    override fun getTestDataPath(): String {
        return "src/test/testData"
    }

    @Test
    fun `create test class from simple class`(){
        val kotlinFile = LocalFileSystem.getInstance().findFileByPath("src/test/testData/SimpleTestData.kt")!!
        val kotlinPsi = psiManager.findFile(kotlinFile)!! as KtFile

        val testResultFile = LocalFileSystem.getInstance().findFileByPath("src/test/testData/SimpleTestDataTest.kt")!!
        val testResultPsi = psiManager.findFile(testResultFile)!! as KtFile


        val newFile = KotlinTestGenerator()
            .generateTest(kotlinPsi, KotlinTestGenerator.TestSettings(true))

        TestCase.assertEquals(testResultPsi.text, newFile.text)
    }

    @Test
    fun renameClassToClassTest(){
        val newFile = PsiFileFactory.getInstance(project).createFileFromText(
            "NewKotlinFile.kt", KotlinFileType.INSTANCE as FileType, "class KotlinClass"
        ) as KtFile

        val testFile = PsiFileFactory.getInstance(project).createFileFromText(
            "NewKotlinFile.kt", KotlinFileType.INSTANCE as FileType, "class KotlinClassTest"
        ) as KtFile

        val testGenerator = KotlinTestGenerator()

        val newTestFile: KtFile = testGenerator.generateTest(newFile)

        TestCase.assertEquals(testFile.text, newTestFile.text)
    }

    @Test
    fun `rename file to classTest_kt`(){
        val newFile = PsiFileFactory.getInstance(project).createFileFromText(
            "NewKotlinFile.kt", KotlinFileType.INSTANCE as FileType, "class KotlinClass"
        ) as KtFile

        val testGenerator = KotlinTestGenerator()

        val newTestFile: KtFile = testGenerator.generateTest(newFile)

        TestCase.assertEquals("KotlinClassTest.kt", newTestFile.name)
    }

    @Test
    fun `annotate methods with @Test`(){
        val newFile = PsiFileFactory.getInstance(project).createFileFromText(
            "NewKotlinFile.kt", KotlinFileType.INSTANCE as FileType,
            "class KotlinClass{\n" +
                    "\tfun method(){}\n" +
                    "}"
        ) as KtFile

        val testGenerator = KotlinTestGenerator()

        val newTestFile: KtFile = testGenerator.generateTest(newFile)




        TestCase.assertEquals(
            "class KotlinClassTest {\n" +
                    "    @org.junit.Test\n" +
                    "    fun method() {\n" +
                    "    }\n" +
                    "}",
            newTestFile.text)
    }

    @Suppress("USELESS_IS_CHECK")
    @Test
    fun `create annotation`(){
        val newFile = PsiFileFactory.getInstance(project).createFileFromText(
            "BasicFile.kt", KotlinFileType.INSTANCE as FileType, ""
        ) as KtFile

        val factory = KtPsiFactory(newFile.project, false)
        val annotation = factory.createAnnotationEntry("@org.junit.Test")
        TestCase.assertTrue(annotation is KtAnnotationEntry)
        TestCase.assertEquals("@org.junit.Test", annotation.text)
    }

    @Test
    fun `remove contents of functions`(){
        val newFile = PsiFileFactory.getInstance(project).createFileFromText(
            "NewKotlinFile.kt", KotlinFileType.INSTANCE as FileType,
            "class KotlinClass{\n" +
                    "\tfun method(){\n" +
                    "\t\tprintln(\"We're doing something\")\n" +
                    "\t}\n" +
                    "}"
        ) as KtFile

        val testGenerator = KotlinTestGenerator()

        val newTestFile: KtFile = testGenerator.generateTest(newFile)

        TestCase.assertEquals(
            "class KotlinClassTest {\n" +
                    "    @org.junit.Test\n" +
                    "    fun method() {\n" +
                    "    }\n" +
                    "}",
            newTestFile.text)
    }

    @Test
    fun `remove params and types of functions`(){
        val newFile = PsiFileFactory.getInstance(project).createFileFromText(
            "NewKotlinFile.kt", KotlinFileType.INSTANCE as FileType,
            "class KotlinClass{\n" +
                    "\tfun method(thing: Int):Boolean{}\n" +
                    "}"
        ) as KtFile

        val testGenerator = KotlinTestGenerator()

        val newTestFile: KtFile = testGenerator.generateTest(newFile)

        TestCase.assertEquals(
            "class KotlinClassTest {\n" +
                    "    @org.junit.Test\n" +
                    "    fun method() {\n" +
                    "    }\n" +
                    "}",
            newTestFile.text)
    }

    @Test
    fun `fun create class reference`(){
        val newFile = PsiFileFactory.getInstance(project).createFileFromText(
            "NewKotlinFile.kt", KotlinFileType.INSTANCE as FileType,
            "class KotlinClass{\n" +
                    "\n" +
                    "}"
        ) as KtFile

        val testGenerator = KotlinTestGenerator()

        val newTestFile: KtFile = testGenerator.generateTest(newFile, KotlinTestGenerator.TestSettings(true))

        TestCase.assertEquals(
            "class KotlinClassTest {\n" +
                    "    val kotlinClass: KotlinClass = KotlinClass()\n" +
                    "\n" +
                    "}",
            newTestFile.text)
    }

    @Test
    fun `don't do weird things when classes have empty constructor`(){
        val newFile = PsiFileFactory.getInstance(project).createFileFromText(
            "NewKotlinFile.kt", KotlinFileType.INSTANCE as FileType,
            "class KotlinClass () {\n" +
                    "\n" +
                    "}"
        ) as KtFile

        val testGenerator = KotlinTestGenerator()

        val newTestFile: KtFile = testGenerator.generateTest(newFile, KotlinTestGenerator.TestSettings(true))

        TestCase.assertEquals(
            "class KotlinClassTest {\n" +
                    "    val kotlinClass: KotlinClass = KotlinClass()\n" +
                    "\n" +
                    "}",
            newTestFile.text)
    }

    @Test
    fun `fun create class reference and call functions`(){
        val newFile = PsiFileFactory.getInstance(project).createFileFromText(
            "NewKotlinFile.kt", KotlinFileType.INSTANCE as FileType,
            "class KotlinClass{\n" +
                    "\tfun doSomething(){\n" +
                    "\t\tprintln(\"do something\")\n" +
                    "\t}\n" +
                    "}"
        ) as KtFile

        val testGenerator = KotlinTestGenerator()

        val newTestFile: KtFile = testGenerator.generateTest(newFile, KotlinTestGenerator.TestSettings(true))

        TestCase.assertEquals(
            "class KotlinClassTest {\n" +
                    "    val kotlinClass: KotlinClass = KotlinClass()\n" +
                    "    @org.junit.Test\n" +
                    "    fun doSomething() {\n" +
                    "        kotlinClass.doSomething()\n" +
                    "    }\n" +
                    "}",
            newTestFile.text)
    }

    @Test
    fun `create class reference, call functions with default param of Int`(){
        val newFile = PsiFileFactory.getInstance(project).createFileFromText(
            "NewKotlinFile.kt", KotlinFileType.INSTANCE as FileType,
            "class KotlinClass{\n" +
                    "\tfun doSomething(thing: Int){\n" +
                    "\t\tprintln(\"do something\")\n" +
                    "\t}\n" +
                    "}"
        ) as KtFile

        val testGenerator = KotlinTestGenerator()

        val newTestFile: KtFile = testGenerator.generateTest(newFile, KotlinTestGenerator.TestSettings(true))

        TestCase.assertEquals(
            "class KotlinClassTest {\n" +
                    "    val kotlinClass: KotlinClass = KotlinClass()\n" +
                    "    @org.junit.Test\n" +
                    "    fun doSomething() {\n" +
                    "        kotlinClass.doSomething(-1)\n" +
                    "    }\n" +
                    "}",
            newTestFile.text)
    }


    @Test
    fun `optimize imports`(){
        val newFile = PsiFileFactory.getInstance(project).createFileFromText(
            "NewKotlinFile.kt", KotlinFileType.INSTANCE as FileType,
            "class KotlinClassTest {\n" +
                    "    val kotlinClass: KotlinClass = KotlinClass()\n" +
                    "    @org.junit.Test\n" +
                    "    fun doSomething() {\n" +
                    "        kotlinClass.doSomething(0)\n" +
                    "    }\n" +
                    "}"
        ) as KtFile

        val newTestFile = KotlinTestGenerator().optimizeImport(newFile)

        TestCase.assertEquals(
            "import org.junit.Test\n\n" +
                    "class KotlinClassTest {\n" +
                    "    val kotlinClass: KotlinClass = KotlinClass()\n" +
                    "    @org.junit.Test\n" +
                    "    fun doSomething() {\n" +
                    "        kotlinClass.doSomething(0)\n" +
                    "    }\n" +
                    "}",
            newTestFile.text)
    }

    @Test
    fun `basic return types handled`(){
        val kotlinFile = LocalFileSystem.getInstance().findFileByPath("src/test/testData/BasicTypes.kt")!!
        val kotlinPsi = psiManager.findFile(kotlinFile)!! as KtFile

        val testResultFile = LocalFileSystem.getInstance().findFileByPath("src/test/testData/BasicTypesTest.kt")!!
        val testResultPsi = psiManager.findFile(testResultFile)!! as KtFile


        val newFile = KotlinTestGenerator()
            .generateTest(kotlinPsi, KotlinTestGenerator.TestSettings(true))

        TestCase.assertEquals(testResultPsi.text, newFile.text)
    }

    @Test
    fun `basic call types handled`(){
        val kotlinFile = LocalFileSystem.getInstance().findFileByPath("src/test/testData/BasicParamTypes.kt")!!
        val kotlinPsi = psiManager.findFile(kotlinFile)!! as KtFile

        val testResultFile = LocalFileSystem.getInstance().findFileByPath("src/test/testData/BasicParamTypesTest.kt")!!
        val testResultPsi = psiManager.findFile(testResultFile)!! as KtFile


        val newFile = KotlinTestGenerator()
            .generateTest(kotlinPsi, KotlinTestGenerator.TestSettings(true))

        TestCase.assertEquals(testResultPsi.text, newFile.text)
    }
}