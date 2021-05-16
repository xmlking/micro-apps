package micro.apps.account

import com.google.protobuf.Any
import com.google.protobuf.StringValue
import io.grpc.Grpc
import io.grpc.ManagedChannel
import io.grpc.TlsChannelCredentials
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withTimeoutOrNull
import micro.apps.account.config.Account
import micro.apps.account.config.TLS
import micro.apps.account.config.config
import micro.apps.proto.account.v1.AccountServiceGrpcKt
import micro.apps.proto.account.v1.SearchRequest
import micro.apps.proto.account.v1.SearchResponse
import micro.apps.proto.common.fixtures.mockPerson
import micro.apps.test.E2E
import micro.apps.test.Slow
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.io.File
import java.security.Security

class AccountServiceTest : FunSpec({
    // Add BCP to avoid `algid parse error, not a sequence` eror
    Security.addProvider(BouncyCastleProvider())

    val port = 5000
    lateinit var server: AccountServer
    lateinit var channel: ManagedChannel

    beforeSpec {
        server = AccountServer(port)
        server.start()
    }

    afterSpec {
        server.server.shutdown()
    }

    beforeTest {
        val creds = TlsChannelCredentials.newBuilder()
            .keyManager(File(config[TLS.clientCert]), File(config[TLS.clientKey]))
            .trustManager(File(config[TLS.caCert])).build()
        channel = Grpc.newChannelBuilder(config[Account.endpoint], creds).overrideAuthority(config[Account.authority]).build()
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
