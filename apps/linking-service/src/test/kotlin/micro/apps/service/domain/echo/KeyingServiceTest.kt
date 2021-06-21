package micro.apps.service.domain.echo

import io.grpc.ManagedChannel
import io.grpc.Server
import io.grpc.inprocess.InProcessChannelBuilder
import io.grpc.inprocess.InProcessServerBuilder
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import micro.apps.proto.common.v1.Address
import micro.apps.proto.common.v1.Profile
import micro.apps.proto.keying.v1.KeyRequest
import micro.apps.proto.keying.v1.KeyResponse
import micro.apps.proto.keying.v1.KeyingServiceGrpcKt.KeyingServiceCoroutineStub
import micro.apps.service.KeyingService
import micro.apps.test.E2E

class KeyingServiceTest : FunSpec({
    lateinit var uniqueName: String
    lateinit var server: Server
    lateinit var channel: ManagedChannel

    beforeSpec {
        uniqueName = InProcessServerBuilder.generateName()
        server = InProcessServerBuilder.forName(uniqueName).directExecutor().addService(KeyingService()).build()
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

    test("should be able to call KeyingService/Key method").config(tags = setOf(E2E)) {
        val keyingStub = KeyingServiceCoroutineStub(channel)

        lateinit var response: KeyResponse

        var address = with(Address.newBuilder()) {
            suite = "1234"
            street = "FourWinds Dr"
            city = "Riverside"
            state = "California"
            country = "USA"
            return@with build()
        }

        shouldNotThrowAny {
            val request = KeyRequest.newBuilder().setProfile(Profile.PROFILE_RO).setAddress(address).build()
            response = keyingStub.key(request)
        }

        response.key shouldBe "123e4567-e89b-12d3-a456-426614174000"
    }
})
