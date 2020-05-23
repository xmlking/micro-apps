package micro.apps.proto.account

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import micro.apps.proto.common.v1.Person

class AccountKtTest : FunSpec({

    test("Person proto generated class should be buildable") {
        val person = with(Person.newBuilder()) {
            firstName = "sumo"
            lastName = "demo"
            phone = "000-000-0000"
            return@with build()
        }
        person.id.isEmpty() shouldBe true
        person.firstName shouldBe "sumo"
    }
})
