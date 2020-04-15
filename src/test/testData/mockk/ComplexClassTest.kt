package mockk

class ComplexClassTest {
    val complexClass: ComplexClass = ComplexClass(SimpleClass())
    @org.junit.Test
    fun doAThing() {
        complexClass.doAThing()
    }

    @org.junit.Test
    fun doAnotherThing() {
        val value = complexClass.doAnotherThing("Dope")
        org.junit.Assert.assertEquals(true, value)
    }
}