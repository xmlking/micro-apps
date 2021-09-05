package micro.apps.proto.account

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import micro.apps.proto.common.v1.person

class AccountKtTest : FunSpec({

    test("Person proto generated class should be buildable") {
        val person = person {
            firstName = "sumo"
            lastName = "demo"
            phone = "000-000-0000"
        }
        person.email.isEmpty() shouldBe true
        person.firstName shouldBe "sumo"
    }
})
