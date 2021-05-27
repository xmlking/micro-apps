package micro.apps.greeting.domain.echo

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import micro.apps.proto.echo.v1.EchoRequest
import micro.apps.proto.echo.v1.EchoResponse
import micro.apps.proto.echo.v1.EchoServiceGrpcKt
import micro.apps.proto.echo.v1.EchoStreamRequest
import micro.apps.proto.echo.v1.EchoStreamResponse
import javax.inject.Singleton

/*
import io.grpc.stub.StreamObserver
import micro.apps.proto.echo.v1.EchoServiceGrpc

@Singleton
class EchoService0 : EchoServiceGrpc.EchoServiceImplBase() {
    override fun echo(request: EchoRequest, responseObserver: StreamObserver<EchoResponse?>) {
        val name: String = request.message
        val message = "Hello $name"
        responseObserver.onNext(EchoResponse.newBuilder().setMessage(message).build())
        responseObserver.onCompleted()
    }
}
*/

@Singleton
class EchoService : EchoServiceGrpcKt.EchoServiceCoroutineImplBase() {
    override suspend fun echo(request: EchoRequest): EchoResponse = EchoResponse.newBuilder()
        .setMessage("Hello ${request.message}")
        .build()

    override fun echoStream(request: EchoStreamRequest): Flow<EchoStreamResponse> = flow {
        while (true) {
            delay(1000)
            emit(EchoStreamResponse.newBuilder().setMessage("hello, ${request.message}").build())
        }
    }
}
