package micro.apps.service.domain.echo

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import micro.apps.proto.echo.v1.EchoRequest
import micro.apps.proto.echo.v1.EchoResponse
import micro.apps.proto.echo.v1.EchoServiceGrpcKt
import micro.apps.proto.echo.v1.EchoStreamRequest
import micro.apps.proto.echo.v1.EchoStreamResponse
import micro.apps.proto.util.EchoResponse
import micro.apps.proto.util.EchoStreamResponse
import mu.KotlinLogging
import org.lognet.springboot.grpc.GRpcService

private val logger = KotlinLogging.logger {}

@GRpcService
class EchoService : EchoServiceGrpcKt.EchoServiceCoroutineImplBase() {
    override suspend fun echo(request: EchoRequest): EchoResponse =
        EchoResponse { message = "Hello ${request.message}" }

    override fun echoStream(request: EchoStreamRequest): Flow<EchoStreamResponse> = flow {
        //noinspection
        logger.atDebug().log("request: {}", request)
        while (true) {
            delay(1000)
            emit(EchoStreamResponse { message = "Hello ${request.message}" })
        }
    }
}
