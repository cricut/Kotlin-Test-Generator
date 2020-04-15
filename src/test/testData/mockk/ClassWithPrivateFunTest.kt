package mockk

class ClassWithPrivateFunTest {
    val classWithPrivateFun: ClassWithPrivateFun = ClassWithPrivateFun()

    @org.junit.Test
    fun publicFun() {
        val value = classWithPrivateFun.publicFun(-1)
        org.junit.Assert.assertEquals(true, value)
    }
}