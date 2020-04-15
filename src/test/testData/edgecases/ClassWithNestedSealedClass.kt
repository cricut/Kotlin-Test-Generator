package com.seboba.testgen.generator.edgecases

class ClassWithNestedSealedClass {
    fun doAThing(){
        println("We did a thing")
    }

    fun doAnotherThing(string: String): Boolean {
        return NestedClass().nestedFun()
    }

    sealed class SealedClass {
        object Instance1: SealedClass()
        object Instance2: SealedClass()
        data class Instance3(val value: Int): SealedClass()
    }
}