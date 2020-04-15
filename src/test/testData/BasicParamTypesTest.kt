class BasicParamTypesTest {
    val basicParamTypes: BasicParamTypes = BasicParamTypes()

    @org.junit.Test
    fun unitFun() {
        basicParamTypes.unitFun(Unit)
    }

    @org.junit.Test
    fun booleanFun() {
        val value = basicParamTypes.booleanFun(true)
        org.junit.Assert.assertEquals(true, value)
    }

    @org.junit.Test
    fun intFun() {
        val value = basicParamTypes.intFun(-1)
        org.junit.Assert.assertEquals(-1, value)
    }

    @org.junit.Test
    fun longFun() {
        val value = basicParamTypes.longFun(-1L)
        org.junit.Assert.assertEquals(-1L, value)
    }

    @org.junit.Test
    fun shortFun() {
        val value = basicParamTypes.shortFun(-1)
        org.junit.Assert.assertEquals((-1).toShort(), value)
    }

    @org.junit.Test
    fun floatFun() {
        val value = basicParamTypes.floatFun(-1f)
        org.junit.Assert.assertEquals(-1f, value)
    }

    @org.junit.Test
    fun doubleFun() {
        val value = basicParamTypes.doubleFun(-1.0)
        org.junit.Assert.assertEquals(-1.0, value, 0.01)
    }

    @org.junit.Test
    fun charFun() {
        val value = basicParamTypes.charFun('a')
        org.junit.Assert.assertEquals('a', value)
    }

    @org.junit.Test
    fun stringFun() {
        val value = basicParamTypes.stringFun("Dope")
        org.junit.Assert.assertEquals("Dope", value)
    }

    @org.junit.Test
    fun byteFun() {
        val value = basicParamTypes.byteFun(-1)
        org.junit.Assert.assertEquals((-1).toByte(), value)
    }
}