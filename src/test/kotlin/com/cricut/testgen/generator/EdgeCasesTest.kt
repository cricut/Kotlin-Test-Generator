package com.cricut.testgen.generator

import com.cricut.testgen.generator.KotlinTestGenerator
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixture4TestCase
import junit.framework.TestCase
import org.jetbrains.kotlin.psi.KtFile
import org.junit.Test

class EdgeCasesTest: LightPlatformCodeInsightFixture4TestCase() {

    override fun getTestDataPath(): String {
        return "src/test/testData/edgecases"
    }

    fun loadKtFileByName(fileName: String): KtFile {
        val kotlinFile = LocalFileSystem.getInstance().findFileByPath("$testDataPath/$fileName.kt")!!
        return psiManager.findFile(kotlinFile)!! as KtFile
    }

    fun testFileByName(fileName: String, testSettings: KotlinTestGenerator.TestSettings = KotlinTestGenerator.TestSettings(true)){
        val kotlinPsi = loadKtFileByName(fileName)
        val testResultPsi = loadKtFileByName("${fileName}Test")

        val newFile = KotlinTestGenerator()
            .generateTest(kotlinPsi, testSettings)

        TestCase.assertEquals(testResultPsi.text, newFile.text)
    }

    @Test
    fun `create test class from class with nested class`(){
        testFileByName("ClassWithNestedClass")
    }

    @Test
    fun `create test class from class with nested sealed class`(){
        testFileByName("ClassWithNestedSealedClass")
    }

    @Test
    fun `create test class from class with companion object`(){
        testFileByName("ClassWithCompanionObject")
    }

    @Test
    fun `create test class from class with protected fun`(){
        testFileByName("ClassWithProtectedFun")
    }

    @Test
    fun `create test class from class with annotation`(){
        testFileByName("ClassWithAnnotation")
    }

    @Test
    fun `create test class from class with init fun`(){
        testFileByName("ClassWithInitFun")
    }

    @Test
    fun `create test class from object`(){
        testFileByName("ClassObject")
    }

    @Test
    fun `create test class from class with relaxed inner mocks`(){
        testFileByName("ClassWithRelaxedInnerMocks", KotlinTestGenerator.TestSettings(true,true))
    }

    @Test
    fun `create test class from class with annotations in the constructor`(){
        testFileByName("ClassWithAnnotationsInConstructor", KotlinTestGenerator.TestSettings(true,true))
    }

}