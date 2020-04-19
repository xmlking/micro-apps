package micro.apps.proto.echo

import kotlin.test.Test
import kotlin.test.assertEquals
import micro.apps.proto.echo.v1.EchoRequest

class AccountKtTest {
    @Test
    fun `Test create EchoRequest`() {
        val account = with(EchoRequest.newBuilder()) {
            message = "sumo"
            return@with build()
        }
        assertEquals(account.message, "sumo")
    }
}
