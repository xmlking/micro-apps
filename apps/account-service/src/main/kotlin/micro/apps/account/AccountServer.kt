package micro.apps.account

import io.grpc.Server
import io.grpc.ServerBuilder
import io.grpc.ServerInterceptors
import micro.apps.proto.interceptors.UnknownStatusInterceptor
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}
class AccountServer(private val port: Int) {
    val server: Server = ServerBuilder
        .forPort(port)
        .addService(AccountService())
        .addService(ServerInterceptors.intercept(AccountService(), UnknownStatusInterceptor()))
        .build()

    fun start() {
        server.start()
        logger.info { "Server started, listening on: $port" }
        Runtime.getRuntime().addShutdownHook(
            Thread {
                logger.atInfo().log("*** shutting down gRPC server since JVM is shutting down")
                this@AccountServer.stop()
                logger.atInfo().log("*** server shut down")
            }
        )
    }

    private fun stop() {
        server.shutdown()
    }

    fun blockUntilShutdown() {
        server.awaitTermination()
    }
}

fun main() {
    // logger.atInfo().withCause(exception).log("Log message with: %s", argument);

    val port = System.getenv("PORT")?.toInt() ?: 8080
    val server = AccountServer(port)
    server.start()
    server.blockUntilShutdown()
}
