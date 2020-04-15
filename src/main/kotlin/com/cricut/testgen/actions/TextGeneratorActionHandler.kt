package com.cricut.testgen.actions

import com.cricut.testgen.generator.KotlinTestGenerator
import com.intellij.codeInsight.CodeInsightActionHandler
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFile
import org.jetbrains.kotlin.idea.core.util.toPsiDirectory
import org.jetbrains.kotlin.idea.util.module
import org.jetbrains.kotlin.idea.util.sourceRoots
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.psi.KtFile

object TextGeneratorActionHandler : CodeInsightActionHandler {
    override fun invoke(project: Project, editor: Editor, file: PsiFile) {
        if(file is KtFile){
            val settings = KotlinTestGenerator.TestSettings(true, true, true)
            val newFile = KotlinTestGenerator()
                    .generateTest(file, settings)

            val directory =
                navToTestDirectoryToMatchFilePackage(
                    file
                )
            val actualNewFIle = directory?.createFile(newFile.name) ?: file.containingDirectory?.createFile(newFile.name)
            actualNewFIle?.add(newFile)
        } else {
            println("Hey we can't generate tests for a none Kotlin File")
        }
    }

    private fun navToTestDirectoryToMatchFilePackage(file: KtFile): PsiDirectory? {
        val testModules = file.module?.sourceRoots?.filter { it.path.contains("test") } ?: emptyList()
        val testRootDirectory = testModules.firstOrNull()?.toPsiDirectory(file.project) ?: return null
        return findOrCreateSubDirectoryInRootDirectoryToMatchFilePackage(
            testRootDirectory,
            file.packageFqName.pathSegments()
        )
    }

    private fun findOrCreateSubDirectoryInRootDirectoryToMatchFilePackage(directory: PsiDirectory, pathSegments: List<Name>): PsiDirectory {
        return if(pathSegments.isEmpty()) directory
        else {
            val dir = directory.findSubdirectory(pathSegments.first().identifier) ?: directory.createSubdirectory(pathSegments.first().identifier)
            findOrCreateSubDirectoryInRootDirectoryToMatchFilePackage(
                dir,
                pathSegments.drop(1)
            )
        }
    }
}