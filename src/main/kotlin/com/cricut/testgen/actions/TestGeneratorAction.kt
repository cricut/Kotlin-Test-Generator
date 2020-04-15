package com.cricut.testgen.actions

import com.intellij.codeInsight.generation.actions.BaseGenerateAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile

class TestGeneratorAction: BaseGenerateAction(TextGeneratorActionHandler) {

    override fun isValidForFile(project: Project, editor: Editor, file: PsiFile): Boolean {
        return true
    }

}


