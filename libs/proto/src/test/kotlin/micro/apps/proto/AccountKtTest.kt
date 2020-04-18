package micro.apps.proto

import kotlin.test.Test
import kotlin.test.assertEquals
import micro.apps.proto.account.v1.GetRequest

class AccountKtTest {
    @Test
    fun `Test create TransactionEvent`() {
        val event = with(GetRequest.newBuilder()) {
//            req = ByteString.copyFrom("Any String you want".toByteArray())
//            rsp = ByteString.EMPTY
            return@with build()
        }
        assertEquals(event.rsp.isEmpty, true)
    }
}
