package micro.apps.account

import com.google.protobuf.Any
import com.google.protobuf.StringValue
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
import micro.apps.proto.account.v1.AccountServiceGrpcKt.AccountServiceCoroutineStub
import micro.apps.proto.account.v1.GetRequest
import micro.apps.proto.account.v1.SearchRequest
class AccountClient(private val channel: ManagedChannel) : Closeable {
    private val stub: AccountServiceCoroutineStub = AccountServiceCoroutineStub(channel)

    suspend fun get(id: String) = coroutineScope {
        val request = GetRequest.newBuilder().setId(StringValue.of(id)).build()
        val response = async { stub.get(request) }
        println("Received from Get: ${response.await().account.firstName}")
    }

    fun search(filter: String) = runBlocking {
        val request = SearchRequest.newBuilder().setFilter(Any.pack(StringValue.of(filter))).build()
        val flow = stub.search(request)
        flow.collect { response ->
            println("Received from Search: ${response.account.firstName}")
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

    val client = AccountClient(
        ManagedChannelBuilder.forAddress("localhost", port)
            .usePlaintext()
            .executor(Dispatchers.Default.asExecutor())
            .build())

    val user = args.singleOrNull() ?: "world"
    client.get(user)

    client.search(user)
}
