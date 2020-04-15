package com.seboba.testgen.generator.edgecases

class ClassWithCompanionObject {
    fun doAThing(){
        println("We did a thing")
    }

    fun doAnotherThing(string: String): Boolean {
        return NestedClass().nestedFun()
    }

    companion object {
        fun nestedFun(): Boolean{
            return false
        }

        private fun private nestedFun(): Boolean{
            return false
        }
    }
}