package com.cricut.testgen.generator.mockK

class SimpleClass {
    fun doSomething(){
        println("We did something")
    }

    fun doSomethingElse(int: Int): Boolean {
        if(int == 2) return true
        return false
    }
}