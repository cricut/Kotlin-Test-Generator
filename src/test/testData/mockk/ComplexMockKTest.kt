package mockk

class ComplexClassTest {
    @io.mockk.impl.annotations.MockK
    lateinit var simpleClass: SimpleClass
    lateinit var complexClass: ComplexClass
    @org.junit.Before
    fun setup() {
        io.mockk.MockKAnnotations.init(this, relaxed = true)
        complexClass = ComplexClass(simpleClass)
    }

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