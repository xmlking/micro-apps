package micro.apps.account

import com.google.common.flogger.FluentLogger
import io.grpc.Server
import io.grpc.ServerBuilder
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import micro.apps.proto.account.v1.AccountServiceGrpcKt
import micro.apps.proto.account.v1.GetRequest
import micro.apps.proto.account.v1.GetResponse
import micro.apps.proto.account.v1.SearchRequest
import micro.apps.proto.account.v1.SearchResponse
import micro.apps.proto.common.v1.Person

var person = with(Person.newBuilder()) {
    firstName = "sumo"
    lastName = "demo"
    phone = "000-000-0000"
    return@with build()
}

class AccountServer(val port: Int) {
    val server: Server = ServerBuilder
        .forPort(port)
        .addService(EchoService())
        .build()

    fun start() {
        server.start()
        logger.atInfo().log("Server started, listening on: %s", port)
        Runtime.getRuntime().addShutdownHook(
            Thread {
                println("*** shutting down gRPC server since JVM is shutting down")
                this@AccountServer.stop()
                println("*** server shut down")
            }
        )
    }

    private fun stop() {
        server.shutdown()
    }

    fun blockUntilShutdown() {
        server.awaitTermination()
    }

    private class AccountService : AccountServiceGrpcKt.AccountServiceCoroutineImplBase() {

        override suspend fun get(request: GetRequest): GetResponse = GetResponse
            .newBuilder()
            .setAccount(person)
            .build()

        override fun search(request: SearchRequest): Flow<SearchResponse> = flow {
            // logger.atInfo().log("firstName: %s, lastName: %s", person.firstName, person.lastName)
            // logger.atInfo().log("filter: %s", request.filter)
            while (true) {
                delay(1000)
                emit(SearchResponse.newBuilder().setAccount(person).build())
            }
        }
    }
}

private val logger = FluentLogger.forEnclosingClass()

fun main() {
    // logger.atInfo().withCause(exception).log("Log message with: %s", argument);

    val port = System.getenv("PORT")?.toInt() ?: 50051
    val server = AccountServer(port)
    server.start()
    server.blockUntilShutdown()
}
