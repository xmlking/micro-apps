package micro.apps.echo

import kotlin.test.Test
import kotlin.test.assertNotNull
import micro.apps.proto.echo.v1.EchoRequest

class EchoServerTest {
    @Test
    fun testAppHasAGreeting() {
        val person = with(EchoRequest.newBuilder()) {
            message = "sumo"
            return@with build()
        }
        assertNotNull(person.message, "sumo")
    }
}
