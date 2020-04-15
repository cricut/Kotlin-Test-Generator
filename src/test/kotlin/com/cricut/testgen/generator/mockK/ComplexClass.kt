package com.cricut.testgen.generator.mockK

class ComplexClass(val simpleClass: SimpleClass) {
    fun doAThing(){
        println("We did a thing")
        simpleClass.doSomething()
    }

    fun doAnotherThing(string: String): Boolean {
        return simpleClass.doSomethingElse(string.length)
    }
}