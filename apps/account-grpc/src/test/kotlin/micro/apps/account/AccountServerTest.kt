package micro.apps.account

import kotlin.test.Test
import kotlin.test.assertNotNull
import micro.apps.proto.common.v1.Person

class AccountServerTest {
    @Test
    fun testAppHasAGreeting() {
        val person = with(Person.newBuilder()) {
            firstName = "sumo"
            lastName = "demo"
            phone = "000-000-0000"
            return@with build()
        }
        assertNotNull(person.firstName, "sumo")
    }
}
