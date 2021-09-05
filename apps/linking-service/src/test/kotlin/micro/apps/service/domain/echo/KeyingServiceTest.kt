package micro.apps.service.domain.echo

import io.grpc.ManagedChannel
import io.grpc.Server
import io.grpc.inprocess.InProcessChannelBuilder
import io.grpc.inprocess.InProcessServerBuilder
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import micro.apps.proto.common.v1.Profile
import micro.apps.proto.common.v1.address
import micro.apps.proto.linking.v1.LinkResponse
import micro.apps.proto.linking.v1.LinkingServiceGrpcKt.LinkingServiceCoroutineStub
import micro.apps.proto.linking.v1.linkRequest
import micro.apps.service.LinkingService
import micro.apps.test.E2E

class KeyingServiceTest : FunSpec({
    lateinit var uniqueName: String
    lateinit var server: Server
    lateinit var channel: ManagedChannel

    beforeSpec {
        uniqueName = InProcessServerBuilder.generateName()
        server = InProcessServerBuilder.forName(uniqueName).directExecutor().addService(LinkingService()).build()
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

    test("should be able to call LinkingService/Link method").config(tags = setOf(E2E)) {
        val linkingStub = LinkingServiceCoroutineStub(channel)

        lateinit var response: LinkResponse

        var address = address {
            suite = "1234"
            street = "FourWinds Dr"
            city = "Riverside"
            state = "California"
            country = "USA"
        }

        shouldNotThrowAny {
            val request = linkRequest {
                profile = Profile.PROFILE_RO
                addresses += address
            }
            response = linkingStub.link(request)
        }

        response.personId shouldBe "123e4567-e89b-12d3-a456-426614174000"
    }
})
