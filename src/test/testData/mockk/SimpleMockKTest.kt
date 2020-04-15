package mockk

import io.mockk.MockKAnnotations
import io.mockk.every
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SimpleMockKTest {

    lateinit var simpleClass: SimpleClass

    @Before
    fun setup(){
        MockKAnnotations.init(this)
        simpleClass = SimpleClass()
    }

    @Test
    fun testAMockThing (){
        simpleClass.doSomething()
    }

    @Test
    fun testSomethingElseMock (){
        assertTrue(simpleClass.doSomethingElse(2))
        assertFalse(simpleClass.doSomethingElse(-1))
    }

}

