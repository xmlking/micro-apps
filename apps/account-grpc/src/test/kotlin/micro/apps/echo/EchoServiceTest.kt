package micro.apps.echo

import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import micro.apps.proto.echo.v1.EchoRequest
import micro.apps.proto.echo.v1.EchoResponse
import micro.apps.proto.echo.v1.EchoServiceGrpcKt

class EchoServiceTest : FunSpec({
    val port = 8080
    lateinit var server: EchoServer
    lateinit var channel: ManagedChannel

    beforeSpec {
        server = EchoServer(port)
        server.start()
    }

    afterSpec {
        server.server.shutdown()
    }

    beforeTest {
        channel = ManagedChannelBuilder.forAddress("localhost", port).usePlaintext().build()
    }

    afterTest {
        channel.shutdownNow()
    }

    test("should be able to call EchoService/Search method") {
        val echoStub: EchoServiceGrpcKt.EchoServiceCoroutineStub = EchoServiceGrpcKt.EchoServiceCoroutineStub(channel)

        lateinit var response: EchoResponse

        shouldNotThrowAny {
            val request = EchoRequest.newBuilder().setMessage("sumo").build()
            response = echoStub.echo(request)
        }

        response.message shouldBe "Hello sumo"
    }
})
