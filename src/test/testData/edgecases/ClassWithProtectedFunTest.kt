package com.seboba.testgen.generator.edgecases

class ClassWithProtectedFunTest {
    val classWithProtectedFun: ClassWithProtectedFun = ClassWithProtectedFun()
    @org.junit.Test
    fun doAThing() {
        classWithProtectedFun.doAThing()
    }

}