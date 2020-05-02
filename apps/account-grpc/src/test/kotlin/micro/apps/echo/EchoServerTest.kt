package micro.apps.echo

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import micro.apps.proto.echo.v1.EchoRequest

class EchoServerTest : FunSpec({

    beforeTest {
        println("Starting test ${it.name}!")
    }
    afterTest {
        println("Finished test ${it.a.name}!")
    }

    test("person should have message") {
        val person = with(EchoRequest.newBuilder()) {
            message = "sumo"
            return@with build()
        }
        person.message shouldBe "sumo"
    }
})
