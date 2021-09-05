package micro.apps.service.domain.address

import com.google.protobuf.StringValue
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import micro.apps.proto.address.v1.AddressServiceGrpcKt
import micro.apps.proto.address.v1.CreateRequest
import micro.apps.proto.address.v1.GetRequest
import micro.apps.proto.address.v1.GetResponse
import micro.apps.proto.address.v1.SearchRequest
import micro.apps.proto.address.v1.SearchResponse
import micro.apps.proto.address.v1.getResponse
import micro.apps.proto.address.v1.searchResponse
import micro.apps.proto.common.v1.address
import mu.KotlinLogging

var address = address {
    state = "1234"
    street = "FourWinds Dr"
    city = "Riverside"
    state = "California"
    country = "USA"
}

private val logger = KotlinLogging.logger {}

class AddressService : AddressServiceGrpcKt.AddressServiceCoroutineImplBase() {

    override suspend fun get(request: GetRequest): GetResponse = getResponse { address = address }

    override suspend fun create(request: CreateRequest): StringValue {
        // TODO("not implemented")
        return StringValue.of("123e4567-e89b-12d3-a456-426614174000")
    }

    override fun search(request: SearchRequest): Flow<SearchResponse> = flow {
        // logger.atInfo().log("firstName: {}, lastName: {}", person.firstName, person.lastName)
        // logger.atDebug().addKeyValue("firstName", person.firstName).addKeyValue("lastName", person.lastName).log("Responding with Person:")
        val filter = request.filter.unpack<StringValue>(StringValue::class.java).value
        logger.atDebug().log("filter type: {}", filter)
        while (true) {
            delay(1000)
            emit(
                searchResponse {
                    id = StringValue.of("123e4567-e89b-12d3-a456-426614174000")
                    address = address
                }
            )
        }
    }
}
