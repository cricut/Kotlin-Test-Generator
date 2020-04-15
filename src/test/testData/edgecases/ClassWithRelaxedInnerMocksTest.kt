package com.seboba.testgen.generator.edgecases

class ClassWithRelaxedInnerMocksTest {
    lateinit var classWithRelaxedInnerMocks: ClassWithRelaxedInnerMocks
    @org.junit.Before
    fun setup() {
        io.mockk.MockKAnnotations.init(this, relaxed = true)
        classWithRelaxedInnerMocks = ClassWithRelaxedInnerMocks()
    }

    @org.junit.Test
    fun doAThing() {
        classWithRelaxedInnerMocks.doAThing(io.mockk.mockk(relaxed = true))
    }
}