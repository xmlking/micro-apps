package micro.apps.echo

import io.grpc.Server
import io.grpc.ServerBuilder
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import micro.apps.proto.echo.v1.EchoRequest
import micro.apps.proto.echo.v1.EchoResponse
import micro.apps.proto.echo.v1.EchoServiceGrpcKt
import micro.apps.proto.echo.v1.EchoStreamRequest
import micro.apps.proto.echo.v1.EchoStreamResponse

class EchoServer(val port: Int) {
    val server: Server = ServerBuilder
        .forPort(port)
        .addService(EchoService())
        .build()

    fun start() {
        server.start()
        println("Server started, listening on $port")
        Runtime.getRuntime().addShutdownHook(
            Thread {
                println("*** shutting down gRPC server since JVM is shutting down")
                this@EchoServer.stop()
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

    private class EchoService : EchoServiceGrpcKt.EchoServiceCoroutineImplBase() {
        override suspend fun echo(request: EchoRequest): EchoResponse = EchoResponse
            .newBuilder()
            .setMessage("Hello ${request.message}")
            .build()

        override fun echoStream(request: EchoStreamRequest): Flow<EchoStreamResponse> = flow {
            while (true) {
                delay(1000)
                emit(EchoStreamResponse.newBuilder().setMessage("hello, ${request.message}").build())
            }
        }
    }
}

fun main() {
    val port = System.getenv("PORT")?.toInt() ?: 8080
    val server = EchoServer(port)
    server.start()
    server.blockUntilShutdown()
}
