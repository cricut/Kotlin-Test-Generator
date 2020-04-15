package mockk

class ClassWithPrivateFun {
    private fun privateFun(){
        println("We did something")
    }

    fun publicFun(int: Int): Boolean {
        if(int == 2) return true
        return false
    }
}