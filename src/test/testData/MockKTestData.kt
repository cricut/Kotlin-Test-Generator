class MockKTestData(simpleTestData: SimpleTestData) {

    fun simpleFun(){
        simpleTestData.simpleFun()
    }

    fun otherFun(int: Int): Boolean{
        return simpleTestData.otherFun(int)
    }

}