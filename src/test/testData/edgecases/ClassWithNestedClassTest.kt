package com.seboba.testgen.generator.edgecases

class ClassWithNestedClassTest {
    val classWithNestedClass: ClassWithNestedClass = ClassWithNestedClass()
    @org.junit.Test
    fun doAThing() {
        classWithNestedClass.doAThing()
    }

    @org.junit.Test
    fun doAnotherThing() {
        val value = classWithNestedClass.doAnotherThing("Dope")
        org.junit.Assert.assertEquals(true, value)
    }

}