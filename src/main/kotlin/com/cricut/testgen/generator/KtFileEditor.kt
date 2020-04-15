package com.cricut.testgen.generator

import com.intellij.psi.PsiElement
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.psi.psiUtil.isPrivate
import org.jetbrains.kotlin.psi.psiUtil.isProtected

object KtFileEditor {
    fun getFunctionsAndDeletePrivateAndProtectedOnes(ktClass: KtClassOrObject): List<KtFunction> {
        val funList = arrayListOf<KtFunction>()
        ktClass.body?.functions?.forEach { element ->
            if (element.isPrivate() || element.isProtected()) {
                element.delete()
            } else {
                funList.add(element)
            }
        }
        return funList
    }

    fun removeConstructors(ktClass: KtClassOrObject) {
        ktClass.children.forEach { psiElement ->
            (psiElement as PsiElement).followPSITree(0) { element, level ->
                if(
                    element is KtPrimaryConstructor ||
                    element is KtSecondaryConstructor
                ) {
                    element.delete()
                }
            }
        }
    }

    fun removeSuperTypeList(ktClass: KtClassOrObject){
        ktClass.getSuperTypeList()?.delete()
    }

    fun removeProperties(ktClass: KtClassOrObject) {
        if(ktClass is KtClass) ktClass.getProperties().forEach { it.delete() }
    }

    fun removeCompanionObjects(ktClass: KtClassOrObject) {
        ktClass.companionObjects.forEach { it.delete() }
    }

    fun removeClassAnnotation(ktClass: KtClassOrObject) {
        ktClass.modifierList?.delete()
    }

    fun removeInitFun(ktClass: KtClassOrObject){
        ktClass.getAnonymousInitializers().forEach { it.delete() }
    }

    fun removeNestedClasses(ktClass: KtClassOrObject) {
        ktClass.body?.children?.forEach { psiElement ->
            if(psiElement is KtClassOrObject) psiElement.delete()
        }
    }

    fun renameFile(file: KtFile, name: String){
        file.name = "$name.kt"
    }

    fun renameClass(ktClass: KtClassOrObject, oldClassName: String?): String {
        val newClassName = "${oldClassName}Test"
        ktClass.setName(newClassName)
        return newClassName
    }
}