package micro.apps.service.domain.echo

import io.grpc.ManagedChannel
import io.grpc.Server
import io.grpc.inprocess.InProcessChannelBuilder
import io.grpc.inprocess.InProcessServerBuilder
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import micro.apps.proto.echo.v1.EchoRequest
import micro.apps.proto.echo.v1.EchoResponse
import micro.apps.proto.echo.v1.EchoServiceGrpcKt
import micro.apps.test.E2E

class EchoServiceTest : FunSpec({
    lateinit var uniqueName: String
    lateinit var server: Server
    lateinit var channel: ManagedChannel

    beforeSpec {
        uniqueName = InProcessServerBuilder.generateName()
        server = InProcessServerBuilder.forName(uniqueName).directExecutor().addService(EchoService()).build()
        server.start()
    }

    afterSpec {
        server.shutdown()
    }

    beforeTest {
        channel = InProcessChannelBuilder.forName(uniqueName).directExecutor().build()
    }

    afterTest {
        channel.shutdownNow()
    }

    test("should be able to call EchoService/Search method").config(tags = setOf(E2E)) {
        val echoStub: EchoServiceGrpcKt.EchoServiceCoroutineStub = EchoServiceGrpcKt.EchoServiceCoroutineStub(channel)

        lateinit var response: EchoResponse

        shouldNotThrowAny {
            val request = EchoRequest.newBuilder().setMessage("sumo").build()
            response = echoStub.echo(request)
        }

        response.message shouldBe "Hello sumo"
    }
})
