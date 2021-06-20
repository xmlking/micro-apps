package micro.apps.service.domain.account

import com.google.protobuf.Any
import com.google.protobuf.StringValue
import io.grpc.ManagedChannel
import io.grpc.Server
import io.grpc.inprocess.InProcessChannelBuilder
import io.grpc.inprocess.InProcessServerBuilder
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withTimeoutOrNull
import micro.apps.proto.account.v1.AccountServiceGrpcKt
import micro.apps.proto.account.v1.SearchRequest
import micro.apps.proto.account.v1.SearchResponse
import micro.apps.proto.common.fixtures.mockPerson
import micro.apps.service.config.config
import micro.apps.test.E2E
import micro.apps.test.Slow
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.Security

class AccountServiceTest : FunSpec({
    // Add BCP to avoid `algid parse error, not a sequence` eror
    Security.addProvider(BouncyCastleProvider())
    lateinit var uniqueName: String
    lateinit var server: Server
    lateinit var channel: ManagedChannel

    beforeSpec {
        uniqueName = InProcessServerBuilder.generateName()
        server = InProcessServerBuilder.forName(uniqueName).directExecutor().addService(AccountService()).build()
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

    test("should be able to call AccountService/Get method").config(tags = setOf(E2E)) {
        val aClient = AccountClient(channel)

        shouldNotThrowAny {
            aClient.get("sumo")
        }

        aClient.close()
    }

    test("should be able to call AccountService/Search method").config(tags = setOf(Slow, E2E)) {
        val accountStub: AccountServiceGrpcKt.AccountServiceCoroutineStub =
            AccountServiceGrpcKt.AccountServiceCoroutineStub(channel)

        lateinit var searchResponse: SearchResponse

        shouldNotThrowAny {
            val request = SearchRequest.newBuilder().setFilter(Any.pack(StringValue.of("filter"))).build()
            val accountFlow = accountStub.search(request)
            withTimeoutOrNull(5000) { // Timeout after 5000ms
                accountFlow.collect { response ->
                    println("Received from Search: ${response.account.firstName}")
                    searchResponse = response
                }
            }
            println("Done")
        }

        searchResponse.account.firstName shouldBe "sumo"
    }

    test("test mockPerson helper") {
        println(mockPerson(1))
    }
})
