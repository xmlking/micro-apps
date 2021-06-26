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
import micro.apps.proto.common.v1.Address
import micro.apps.proto.common.v1.Profile
import micro.apps.proto.keying.v1.KeyRequest
import micro.apps.proto.keying.v1.KeyingServiceGrpcKt.KeyingServiceCoroutineStub
import micro.apps.proto.util.KeyRequest
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

class KeyingClient(private val channel: ManagedChannel) : Closeable {
    private val stub: KeyingServiceCoroutineStub = KeyingServiceCoroutineStub(channel)

    suspend fun key(streetNumber: String, streetName: String) = coroutineScope {
        var address = with(Address.newBuilder()) {
            suite = streetNumber
            street = streetName
            city = "Riverside"
            state = "California"
            country = "USA"
            return@with build()
        }

        val request = KeyRequest { profile = Profile.PROFILE_RO; address = address }
        val response = async { stub.key(request) }
        println("Received: ${response.await().key}")
    }

    fun keyStream(streetNumber: String, streetName: String) = runBlocking {
        var address = with(Address.newBuilder()) {
            suite = streetNumber
            street = streetName
            city = "Riverside"
            state = "California"
            country = "USA"
            return@with build()
        }

        val requests: Flow<KeyRequest> = flow {
            repeat(3) {
                val request = KeyRequest { profile = Profile.PROFILE_RO; address = address }
                emit(request)
                delay(500)
            }
        }
        val flow = stub.keyStream(requests)
        flow.collect { response ->
            println("Received: ${response.key}")
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

    val client = KeyingClient(channel)

    val suite = args.singleOrNull() ?: "1234"
    client.key(suite, "FourWinds Dr")

    client.keyStream(suite, "FourWinds Dr")
}
