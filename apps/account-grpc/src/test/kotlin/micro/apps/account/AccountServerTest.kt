package micro.apps.account

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import micro.apps.proto.common.v1.Person

class AccountServerTest : FunSpec({

    test("proto generated class can be used") {
        val person = with(Person.newBuilder()) {
            firstName = "sumo"
            lastName = "demo"
            phone = "000-000-0000"
            return@with build()
        }
        person.firstName shouldBe "sumo"
    }
})
