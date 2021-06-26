package micro.apps.service.domain.echo

import io.grpc.Grpc
import io.grpc.ManagedChannel
import io.grpc.TlsChannelCredentials
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import micro.apps.proto.echo.v1.EchoServiceGrpcKt.EchoServiceCoroutineStub
import micro.apps.proto.util.EchoRequest
import micro.apps.proto.util.EchoStreamRequest
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

class EchoClient(private val channel: ManagedChannel) : Closeable {
    private val stub: EchoServiceCoroutineStub = EchoServiceCoroutineStub(channel)

    suspend fun echo(name: String) = coroutineScope {
        val request = EchoRequest { message = name }
        val response = async { stub.echo(request) }
        println("Received: ${response.await().message}")
    }

    fun echoStream(name: String) = runBlocking {
        val request = EchoStreamRequest { message = name }
        val flow = stub.echoStream(request)
        flow.collect { response ->
            println("Received: ${response.message}")
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

    val client = EchoClient(channel)

    val user = args.singleOrNull() ?: "world"
    client.echo(user)

    client.echoStream(user)
}
