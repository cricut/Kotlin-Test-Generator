package com.cricut.testgen.generator.mockK

class BasicTypesTest {
    val basicTypes: BasicTypes =
        BasicTypes()

    @org.junit.Test
    fun unitFun() {
        basicTypes.unitFun()
    }

    @org.junit.Test
    fun booleanFun() {
        val value = basicTypes.booleanFun()
        org.junit.Assert.assertEquals(true, value)
    }

    @org.junit.Test
    fun intFun() {
        val value = basicTypes.intFun()
        org.junit.Assert.assertEquals(-1, value)
    }

    @org.junit.Test
    fun longFun() {
        val value = basicTypes.longFun()
        org.junit.Assert.assertEquals(-1L, value)
    }

    @org.junit.Test
    fun shortFun() {
        val value = basicTypes.shortFun()
        org.junit.Assert.assertEquals((-1).toShort(), value)
    }

    @org.junit.Test
    fun floatFun() {
        val value = basicTypes.floatFun()
        org.junit.Assert.assertEquals(-1f, value)
    }

    @org.junit.Test
    fun doubleFun() {
        val value = basicTypes.doubleFun()
        org.junit.Assert.assertEquals(-1.0, value, 0.01)
    }

    @org.junit.Test
    fun charFun() {
        val value = basicTypes.charFun()
        org.junit.Assert.assertEquals('a', value)
    }

    @org.junit.Test
    fun stringFun() {
        val value = basicTypes.stringFun()
        org.junit.Assert.assertEquals("Dope", value)
    }

    @org.junit.Test
    fun byteFun() {
        val value = basicTypes.byteFun()
        org.junit.Assert.assertEquals((-1).toByte(), value)
    }
}