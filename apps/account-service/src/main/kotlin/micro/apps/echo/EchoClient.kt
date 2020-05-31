package micro.apps.echo

import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import java.io.Closeable
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import micro.apps.proto.echo.v1.EchoRequest
import micro.apps.proto.echo.v1.EchoServiceGrpcKt.EchoServiceCoroutineStub
import micro.apps.proto.echo.v1.EchoStreamRequest

class EchoClient(private val channel: ManagedChannel) : Closeable {
    private val stub: EchoServiceCoroutineStub = EchoServiceCoroutineStub(channel)

    suspend fun echo(name: String) = coroutineScope {
        val request = EchoRequest.newBuilder().setMessage(name).build()
        val response = async { stub.echo(request) }
        println("Received: ${response.await().message}")
    }

    fun echoStream(name: String) = runBlocking {
        val request = EchoStreamRequest.newBuilder().setMessage(name).build()
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
    val port = 8080

    val client = EchoClient(
        ManagedChannelBuilder.forAddress("localhost", port)
            .usePlaintext()
            .executor(Dispatchers.Default.asExecutor())
            .build())

    val user = args.singleOrNull() ?: "world"
    client.echo(user)

    client.echoStream(user)
}
