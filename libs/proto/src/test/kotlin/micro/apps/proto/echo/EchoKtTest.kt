package micro.apps.proto.echo

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import micro.apps.proto.echo.v1.EchoRequest

class EchoKtTest : FunSpec({

    test("EchoRequest proto generated class should be buildable") {
        val account = with(EchoRequest.newBuilder()) {
            message = "sumo"
            return@with build()
        }
        account.message shouldBe "sumo"
    }
})
