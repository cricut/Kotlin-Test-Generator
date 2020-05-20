package com.cricut.testgen.generator.edgecases

class ClassWithNestedClass {
    fun doAThing(){
        println("We did a thing")
    }

    fun doAnotherThing(string: String): Boolean {
        println("We did another thing")
        return NestedClass().nestedFun()
    }

    class NestedClass(){

        fun nestedFun(): Boolean{
            return false
        }
    }
}