package com.cricut.testgen.generator

import com.intellij.psi.PsiElement

fun PsiElement.followPSITree(level: Int, function: (PsiElement, Int) -> Unit){
    function.invoke(this, level)
    children.forEach { it.followPSITree( level + 1,function) }
}