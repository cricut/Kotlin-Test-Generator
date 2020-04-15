package com.seboba.testgen.generator.edgecases

class ClassWithCompanionObjectTest {
    val classWithCompanionObject: ClassWithCompanionObject = ClassWithCompanionObject()
    @org.junit.Test
    fun doAThing() {
        classWithCompanionObject.doAThing()
    }

    @org.junit.Test
    fun doAnotherThing() {
        val value = classWithCompanionObject.doAnotherThing("Dope")
        org.junit.Assert.assertEquals(true, value)
    }

}