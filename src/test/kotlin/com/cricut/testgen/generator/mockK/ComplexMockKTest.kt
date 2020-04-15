package com.cricut.testgen.generator.mockK

class ComplexMockKTest {

    @io.mockk.impl.annotations.MockK
    lateinit var mockedSimpleClass: SimpleClass

    lateinit var complexClassWithMock: ComplexClass

    @org.junit.Before
    fun setup() {
        io.mockk.MockKAnnotations.init(this)
        complexClassWithMock = ComplexClass(mockedSimpleClass)
    }

    @org.junit.Test
    fun testAMockThing() {
        io.mockk.every { mockedSimpleClass.doSomething() } returns Unit
        complexClassWithMock.doAThing()
    }

    @org.junit.Test
    fun testSomethingElseMock() {
        io.mockk.every { mockedSimpleClass.doSomethingElse(any()) } returns true
        val value = complexClassWithMock.doAnotherThing("String")
        org.junit.Assert.assertEquals(true, value)
    }

}

