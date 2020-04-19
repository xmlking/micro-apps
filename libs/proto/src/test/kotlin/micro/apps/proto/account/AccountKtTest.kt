package micro.apps.proto.account

import kotlin.test.Test
import kotlin.test.assertEquals
import micro.apps.proto.common.v1.Person

class AccountKtTest {
    @Test
    fun `Test create Person`() {
        val person = with(Person.newBuilder()) {
            firstName = "sumo"
            lastName = "demo"
            phone = "000-000-0000"
            return@with build()
        }
        assertEquals(person.id.isEmpty(), true)
        assertEquals(person.firstName, "sumo")
    }
}
