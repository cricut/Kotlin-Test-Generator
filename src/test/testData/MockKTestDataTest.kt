class MockKTestDataTest {

    @MockK
    val

    val mockKData: MockKTestData = MockKTestData()

    @org.junit.Before() {
        setu
    }

    @org.junit.Test
    fun simpleFun() {
        mockKData.simpleFun()
    }

    @org.junit.Test
    fun otherFun() {
        mockKData.otherFun(0)
    }

}