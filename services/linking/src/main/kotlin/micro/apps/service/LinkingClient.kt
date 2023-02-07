package micro.apps.service

import io.grpc.Grpc
import io.grpc.ManagedChannel
import io.grpc.TlsChannelCredentials
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import micro.apps.proto.common.v1.Profile
import micro.apps.proto.common.v1.address
import micro.apps.proto.common.v1.person
import micro.apps.proto.linking.v1.LinkRequest
import micro.apps.proto.linking.v1.LinkingServiceGrpcKt.LinkingServiceCoroutineStub
import micro.apps.proto.linking.v1.linkRequest
import micro.apps.service.config.Account
import micro.apps.service.config.TLS
import micro.apps.service.config.config
import mu.KotlinLogging
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.io.Closeable
import java.io.File
import java.security.Security
import java.util.concurrent.TimeUnit

private val logger = KotlinLogging.logger {}

class LinkingClient(private val channel: ManagedChannel) : Closeable {
    private val stub: LinkingServiceCoroutineStub = LinkingServiceCoroutineStub(channel)

    suspend fun key(streetNumber: String, streetName: String) = coroutineScope {
        val person = person {
            firstName = "sumo1"
            lastName = "demo"
            phone = "000-000-0000"
            email = "sumo@demo.com"
        }

        val address = address {
            suite = streetNumber
            street = streetName
            city = "Riverside"
            state = "California"
            country = "USA"
        }

        val request = linkRequest {
            profile = Profile.PROFILE_RO
            this.person = person
            addresses += address
        }
        val response = async { stub.link(request) }
        println("Received: ${response.await().personId}")
    }

    fun keyStream(streetNumber: String, streetName: String) = runBlocking {
        val person = person {
            firstName = "sumo"
            lastName = "demo"
            phone = "000-000-0000"
            email = "sumo@demo.com"
        }

        val address = address {
            suite = streetNumber
            street = streetName
            city = "Riverside"
            state = "California"
            country = "USA"
        }

        val requests: Flow<LinkRequest> = flow {
            repeat(3) {
                val request = linkRequest {
                    profile = Profile.PROFILE_RO
                    this.person = person
                    addresses += address
                }
                emit(request)
                delay(500)
            }
        }
        val flow = stub.linkStream(requests)
        flow.collect { response ->
            println("Received: ${response.personId}")
        }
    }

    override fun close() {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS)
    }
}

/**
 * Echo, uses first argument as name to greet if present;
 * greets "world" otherwise.
 */
fun main(args: Array<String>) = runBlocking {
    // Add BCP to avoid `algid parse error, not a sequence` eror
    Security.addProvider(BouncyCastleProvider())

    val creds = TlsChannelCredentials.newBuilder()
        .keyManager(File(config[TLS.clientCert]), File(config[TLS.clientKey]))
        .trustManager(File(config[TLS.caCert])).build()

    val channel = Grpc.newChannelBuilder(config[Account.endpoint], creds)
        .overrideAuthority(config[Account.authority])
        // .executor(Dispatchers.Default.asExecutor())
        .build()

    val client = LinkingClient(channel)

    val suite = args.singleOrNull() ?: "1234"
    client.key(suite, "FourWinds Dr")

    client.keyStream(suite, "FourWinds Dr")
}
