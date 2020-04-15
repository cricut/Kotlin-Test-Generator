package com.seboba.testgen.generator.edgecases

class ClassWithNestedSealedClassTest {
    val classWithNestedSealedClass: ClassWithNestedSealedClass = ClassWithNestedSealedClass()
    @org.junit.Test
    fun doAThing() {
        classWithNestedSealedClass.doAThing()
    }

    @org.junit.Test
    fun doAnotherThing() {
        val value = classWithNestedSealedClass.doAnotherThing("Dope")
        org.junit.Assert.assertEquals(true, value)
    }

}