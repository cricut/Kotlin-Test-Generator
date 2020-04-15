class SimpleTestDataTest {
    val simpleTestData: SimpleTestData = SimpleTestData()

    @org.junit.Test
    fun simpleFun() {
        simpleTestData.simpleFun()
    }

    @org.junit.Test
    fun otherFun() {
        val value = simpleTestData.otherFun(-1)
        org.junit.Assert.assertEquals(true, value)
    }

}