package micro.apps.service.domain.order

import com.google.protobuf.Empty
import com.google.protobuf.StringValue
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import micro.apps.proto.common.v1.Currency
import micro.apps.proto.common.v1.Product
import micro.apps.proto.order.v1.CreateRequest
import micro.apps.proto.order.v1.GetRequest
import micro.apps.proto.order.v1.GetResponse
import micro.apps.proto.order.v1.ProductServiceGrpcKt
import micro.apps.proto.order.v1.SearchRequest
import micro.apps.proto.order.v1.SearchResponse
import micro.apps.proto.utils.GetProductResponse
import micro.apps.proto.utils.SearchProductResponse
import mu.KotlinLogging

var product = with(Product.newBuilder()) {
    id = "123e4567-e89b-12d3-a456-426614174000"
    name = "watch"
    description = "apple watch"
    currency = Currency.CURRENCY_USD_UNSPECIFIED
    return@with build()
}

private val logger = KotlinLogging.logger {}

class ProductService : ProductServiceGrpcKt.ProductServiceCoroutineImplBase() {

    override suspend fun get(request: GetRequest): GetResponse = GetProductResponse { product = product }

    override suspend fun create(request: CreateRequest): Empty {
        // TODO("not implemented")
        return Empty.getDefaultInstance()
    }

    override fun search(request: SearchRequest): Flow<SearchResponse> = flow {
        // logger.atInfo().log("firstName: {}, lastName: {}", person.firstName, person.lastName)
        // logger.atDebug().addKeyValue("firstName", person.firstName).addKeyValue("lastName", person.lastName).log("Responding with Person:")
        val filter = request.filter.unpack<StringValue>(StringValue::class.java).value
        logger.atDebug().log("filter type: {}", filter)
        while (true) {
            delay(1000)
            emit(SearchProductResponse { product = product })
        }
    }
}
