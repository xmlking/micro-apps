package micro.apps.proto.echo

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import micro.apps.proto.echo.v1.echoRequest

class EchoKtTest : FunSpec({

    test("EchoRequest proto generated class should be buildable") {
        val echo = echoRequest {
            message = "sumo"
        }
        echo.message shouldBe "sumo"
    }
})
