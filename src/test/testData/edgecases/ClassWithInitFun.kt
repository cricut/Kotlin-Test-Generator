package com.seboba.testgen.generator.edgecases

class ClassWithInitFun {
    fun doAThing(){
        println("We did a thing")
    }

    init {
        doAThing()
    }
}