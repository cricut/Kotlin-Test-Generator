package com.seboba.testgen.generator.edgecases

class ClassWithAnnotationTest {
    val classWithAnnotation: ClassWithAnnotation = ClassWithAnnotation()
    @org.junit.Test
    fun doAThing() {
        classWithAnnotation.doAThing()
    }
}