package com.cricut.testgen.generator.mockK

class SimpleMockKTest {

    lateinit var simpleClass: SimpleClass

    @org.junit.Before
    fun setup(){
        io.mockk.MockKAnnotations.init(this)
        simpleClass = SimpleClass()
    }

    @org.junit.Test
    fun testAMockThing (){
        simpleClass.doSomething()
    }

    @org.junit.Test
    fun testSomethingElseMock (){
        kotlin.test.assertTrue(simpleClass.doSomethingElse(2))
        kotlin.test.assertFalse(simpleClass.doSomethingElse(-1))
    }

}

