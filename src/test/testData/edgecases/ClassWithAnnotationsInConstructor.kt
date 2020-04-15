package com.seboba.testgen.generator.edgecases

class ClassWithAnnotationsInConstructor(
    @Reloader(Pen::class) private val requestPenLoad: () -> Unit
) {
    fun doAThing(){
        println("We did a thing")
    }
}