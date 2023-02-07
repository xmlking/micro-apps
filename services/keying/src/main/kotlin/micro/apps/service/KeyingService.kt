package micro.apps.service

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import micro.apps.proto.common.v1.Status
import micro.apps.proto.keying.v1.KeyRequest
import micro.apps.proto.keying.v1.KeyResponse
import micro.apps.proto.keying.v1.KeyingServiceGrpcKt
import micro.apps.proto.keying.v1.keyResponse
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

class KeyingService : KeyingServiceGrpcKt.KeyingServiceCoroutineImplBase() {
    override suspend fun key(request: KeyRequest): KeyResponse = keyResponse {
        key = "123e4567-e89b-12d3-a456-426614174000"
        status = Status.STATUS_NEW
    }

    override fun keyStream(requests: Flow<KeyRequest>): Flow<KeyResponse> = flow {
        requests.collect { request ->
            logger.atDebug().log("request: {}", request)
            delay(1000)
            emit(keyResponse { key = "123e4567-e89b-12d3-a456-426614174000"; status = Status.STATUS_NEW })
        }
    }
}
