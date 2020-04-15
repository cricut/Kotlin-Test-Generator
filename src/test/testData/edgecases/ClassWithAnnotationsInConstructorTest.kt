package com.seboba.testgen.generator.edgecases

class ClassWithAnnotationsInConstructorTest {
    @io.mockk.impl.annotations.MockK
    lateinit var function: () -> Unit
    lateinit var classWithAnnotationsInConstructor: ClassWithAnnotationsInConstructor
    @org.junit.Before
    fun setup() {
        io.mockk.MockKAnnotations.init(this, relaxed = true)
        classWithAnnotationsInConstructor = ClassWithAnnotationsInConstructor(() -> Unit)
    }

    @org.junit.Test
    fun doAThing() {
        classWithAnnotationsInConstructor.doAThing()
    }
}