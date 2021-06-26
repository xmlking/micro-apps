package micro.apps.service.domain.account

import com.google.protobuf.StringValue
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import micro.apps.proto.account.v1.AccountServiceGrpcKt
import micro.apps.proto.account.v1.CreateRequest
import micro.apps.proto.account.v1.GetRequest
import micro.apps.proto.account.v1.GetResponse
import micro.apps.proto.account.v1.SearchRequest
import micro.apps.proto.account.v1.SearchResponse
import micro.apps.proto.common.v1.Person
import micro.apps.proto.util.GetAccountResponse
import micro.apps.proto.util.SearchAccountResponse
import mu.KotlinLogging

var person = with(Person.newBuilder()) {
    firstName = "sumo"
    lastName = "demo"
    phone = "000-000-0000"
    return@with build()
}

private val logger = KotlinLogging.logger {}

class AccountService : AccountServiceGrpcKt.AccountServiceCoroutineImplBase() {

    override suspend fun get(request: GetRequest): GetResponse = GetAccountResponse { account = person }

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
                SearchAccountResponse {
                    id = StringValue.of("123e4567-e89b-12d3-a456-426614174000")
                    account = person
                }
            )
        }
    }
}
