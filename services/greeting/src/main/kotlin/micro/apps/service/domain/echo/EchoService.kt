package micro.apps.service.domain.echo

import io.quarkus.grpc.GrpcService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import micro.apps.proto.echo.v1.EchoRequest
import micro.apps.proto.echo.v1.EchoResponse
import micro.apps.proto.echo.v1.EchoServiceGrpcKt
import micro.apps.proto.echo.v1.EchoStreamRequest
import micro.apps.proto.echo.v1.EchoStreamResponse
import micro.apps.proto.echo.v1.echoResponse
import micro.apps.proto.echo.v1.echoStreamResponse
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

/*
import io.grpc.stub.StreamObserver
import micro.apps.proto.echo.v1.EchoServiceGrpc

@GrpcService
class EchoService0 : EchoServiceGrpc.EchoServiceImplBase() {
    override fun echo(request: EchoRequest, responseObserver: StreamObserver<EchoResponse?>) {
        val name: String = request.message
        val message = "Hello $name"
        responseObserver.onNext(EchoResponse.newBuilder().setMessage(message).build())
        responseObserver.onCompleted()
    }
}
*/

@GrpcService
class EchoService : EchoServiceGrpcKt.EchoServiceCoroutineImplBase() {
    override suspend fun echo(request: EchoRequest): EchoResponse = echoResponse {
        message = "Hello ${request.message}"
    }

    override fun echoStream(request: EchoStreamRequest): Flow<EchoStreamResponse> = flow {
        while (true) {
            logger.error { "xxx" }
            // logger.atError().addKeyValue("hh", "ffff").log("xxx")
            delay(1000)
            emit(echoStreamResponse { message = "hello, ${request.message}" })
        }
    }
}
