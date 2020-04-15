package com.cricut.testgen.generator.mockK

class ClassWithDependencyTest {
    val complexClass: ComplexClass =
        ComplexClass(SimpleClass())
    @org.junit.Test
    fun doAThing() {
        complexClass.doAThing()
    }

    @org.junit.Test
    fun doAnotherThing() {
        val value = complexClass.doAnotherThing("String")
        org.junit.Assert.assertEquals(false, value)
    }

}