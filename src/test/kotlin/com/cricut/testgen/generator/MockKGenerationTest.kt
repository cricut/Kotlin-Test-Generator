package com.cricut.testgen.generator

import com.cricut.testgen.generator.KotlinTestGenerator
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixture4TestCase
import junit.framework.TestCase
import org.jetbrains.kotlin.psi.KtFile
import org.junit.Test

class MockKGenerationTest: LightPlatformCodeInsightFixture4TestCase() {

    override fun getTestDataPath(): String {
        return "src/test/testData/mockk"
    }

    fun loadKtFileByName(fileName: String): KtFile {
        val kotlinFile = LocalFileSystem.getInstance().findFileByPath("$testDataPath/$fileName.kt")!!
        return psiManager.findFile(kotlinFile)!! as KtFile
    }

    @Test
    fun `create test class from simple class`(){
        val kotlinPsi = loadKtFileByName("ComplexClass")
        val testResultPsi = loadKtFileByName("ComplexClassTest")

        val testSettings = KotlinTestGenerator.TestSettings(true)
        val newFile = KotlinTestGenerator()
            .generateTest(kotlinPsi, testSettings)

        TestCase.assertEquals(testResultPsi.text, newFile.text)
    }

    @Test
    fun `create test class from simple class with failing test`(){
        val kotlinPsi = loadKtFileByName("ComplexClass")
        val testResultPsi = loadKtFileByName("ComplexClassFailingTest")

        val testSettings = KotlinTestGenerator.TestSettings(true, false, true)
        val newFile = KotlinTestGenerator()
            .generateTest(kotlinPsi, testSettings)

        TestCase.assertEquals(testResultPsi.text, newFile.text)
    }

    @Test
    fun `create mockk test class from simple class`(){
        val kotlinPsi = loadKtFileByName("ComplexClass")
        val testResultPsi = loadKtFileByName("ComplexMockKTest")

        val testSettings = KotlinTestGenerator.TestSettings(true, true, false)
        val newFile = KotlinTestGenerator()
            .generateTest(kotlinPsi, testSettings)

        TestCase.assertEquals(testResultPsi.text, newFile.text)
    }

    @Test
    fun `class with private fun doesn't test that function`(){
        val kotlinPsi = loadKtFileByName("ClassWithPrivateFun")
        val testResultPsi = loadKtFileByName("ClassWithPrivateFunTest")

        val testSettings = KotlinTestGenerator.TestSettings(true, false, false)
        val newFile = KotlinTestGenerator()
            .generateTest(kotlinPsi, testSettings)

        TestCase.assertEquals(testResultPsi.text, newFile.text)
    }

}