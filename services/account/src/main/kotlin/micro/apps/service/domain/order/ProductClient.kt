package micro.apps.service.domain.order

import com.google.protobuf.Any
import com.google.protobuf.StringValue
import io.grpc.Grpc
import io.grpc.ManagedChannel
import io.grpc.TlsChannelCredentials
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import micro.apps.proto.order.v1.ProductServiceGrpcKt.ProductServiceCoroutineStub
import micro.apps.proto.order.v1.getRequest
import micro.apps.proto.order.v1.searchRequest
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

class ProductClient(private val channel: ManagedChannel) : Closeable {
    private val stub: ProductServiceCoroutineStub = ProductServiceCoroutineStub(channel)

    suspend fun get(idReq: String) = coroutineScope {
        val request = getRequest { id = StringValue.of(idReq) }
        val response = async { stub.get(request) }
        println("Received from Get: ${response.await().product.name}")
    }

    fun search(filterReq: String) = runBlocking {
        val request = searchRequest { filter = Any.pack(StringValue.of(filterReq)) }
        val flow = stub.search(request)
        flow.collect { response ->
            println("Received from Search: ${response.product.name}")
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

    val client = ProductClient(channel)

    val user = args.singleOrNull() ?: "world"
    client.get(user)

    client.search(user)
}
