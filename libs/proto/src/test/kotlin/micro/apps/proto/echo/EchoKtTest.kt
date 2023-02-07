package micro.apps.proto.echo

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import micro.apps.proto.echo.v1.echoRequest
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}
class EchoKtTest : FunSpec({

    test("EchoRequest proto generated class should be buildable") {
        val echo = echoRequest {
            message = "sumo"
        }
        logger.atDebug().addArgument(echo).log("In EchoKtTest")
        echo.message shouldBe "sumo"
    }
})
