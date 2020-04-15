package com.cricut.testgen.actions

import com.cricut.testgen.actions.TestGeneratorAction
import com.cricut.testgen.actions.TextGeneratorActionHandler
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.testFramework.HeavyPlatformTestCase
import com.intellij.testFramework.fixtures.IdeaTestFixtureFactory
import com.intellij.testFramework.fixtures.JavaTestFixtureFactory
import org.jetbrains.kotlin.psi.KtFile


class TestGeneratorActionTest: HeavyPlatformTestCase() {

    fun `test do something`(){

        val projectBuilder = IdeaTestFixtureFactory.getFixtureFactory().createFixtureBuilder(name)

        val myFixture = JavaTestFixtureFactory.getFixtureFactory().createCodeInsightFixture(projectBuilder.fixture)

        myFixture.testAction(TestGeneratorAction())
    }

    fun `do not test action handler`(){
        val projectBuilder = IdeaTestFixtureFactory.getFixtureFactory().createFixtureBuilder(name)

        val myFixture = JavaTestFixtureFactory.getFixtureFactory().createCodeInsightFixture(projectBuilder.fixture)

        myFixture.testDataPath = "src/test/testData"
        myFixture.setUp()

        val kotlinFile = LocalFileSystem.getInstance().findFileByPath("src/test/testData/SimpleTestData.kt")!!
        val kotlinPsi = psiManager.findFile(kotlinFile)!! as KtFile
        myFixture.openFileInEditor(kotlinPsi.virtualFile)

        TextGeneratorActionHandler.invoke(project, myFixture.editor, kotlinPsi)
    }
}