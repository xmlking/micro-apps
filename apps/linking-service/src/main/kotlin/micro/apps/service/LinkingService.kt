package micro.apps.service

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import micro.apps.proto.common.v1.Status
import micro.apps.proto.linking.v1.LinkRequest
import micro.apps.proto.linking.v1.LinkResponse
import micro.apps.proto.linking.v1.LinkingServiceGrpcKt
import micro.apps.proto.util.LinkResponse
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

class LinkingService : LinkingServiceGrpcKt.LinkingServiceCoroutineImplBase() {
    override suspend fun link(request: LinkRequest): LinkResponse = LinkResponse {
        personId = "123e4567-e89b-12d3-a456-426614174000"
        status = Status.STATUS_NEW
    }

    override fun linkStream(requests: Flow<LinkRequest>): Flow<LinkResponse> = flow {
        requests.collect { request ->
            logger.atDebug().log("request: {}", request)
            delay(1000)
            emit(LinkResponse { personId = "123e4567-e89b-12d3-a456-426614174000"; status = Status.STATUS_NEW })
        }
    }
}
