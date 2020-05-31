package micro.apps.account

import com.google.protobuf.Any
import com.google.protobuf.StringValue
import io.grpc.ManagedChannel
import java.io.Closeable
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import micro.apps.proto.account.v1.AccountServiceGrpcKt.AccountServiceCoroutineStub

class AccountClient(private val channel: ManagedChannel) : Closeable {
    private val stub: AccountServiceCoroutineStub = AccountServiceCoroutineStub(channel)

    suspend fun get(idReq: String) = coroutineScope {
        val request = GetRequest { id = StringValue.of(idReq) }
        val response = async { stub.get(request) }
        println("Received from Get: ${response.await().account.firstName}")
    }

    fun search(filterReq: String) = runBlocking {
        val request = SearchRequest { filter = Any.pack(StringValue.of(filterReq)) }
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
    val accountTarget: String = System.getenv("ACCOUNT_SERVICE_TARGET") ?: "localhost:8080"

    val client = AccountClient(channelForTarget(accountTarget))

    val user = args.singleOrNull() ?: "world"
    client.get(user)

    client.search(user)
}
