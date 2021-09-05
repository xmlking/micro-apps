package micro.apps.service.domain.account

import com.google.protobuf.StringValue
import com.google.protobuf.util.FieldMaskUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import micro.apps.proto.account.v1.AccountServiceGrpcKt
import micro.apps.proto.account.v1.CreateRequest
import micro.apps.proto.account.v1.GetRequest
import micro.apps.proto.account.v1.GetResponse
import micro.apps.proto.account.v1.SearchRequest
import micro.apps.proto.account.v1.SearchResponse
import micro.apps.proto.account.v1.getResponse
import micro.apps.proto.account.v1.searchResponse
import micro.apps.proto.common.v1.Person
import micro.apps.proto.common.v1.person
import mu.KotlinLogging

var person = person {
    firstName = "sumo"
    lastName = "demo"
    phone = "000-000-0000"
}

private val logger = KotlinLogging.logger {}

class AccountService : AccountServiceGrpcKt.AccountServiceCoroutineImplBase() {

    override suspend fun get(request: GetRequest): GetResponse = getResponse { account = person }

    override suspend fun create(request: CreateRequest): StringValue {
        // TODO("not implemented")
        return StringValue.of("123e4567-e89b-12d3-a456-426614174000")
    }

    override fun search(request: SearchRequest): Flow<SearchResponse> = flow {
        val filteredPerson = Person.newBuilder()
        FieldMaskUtil.merge(request.fieldMask, person, filteredPerson)

        // logger.atInfo().log("firstName: {}, lastName: {}", person.firstName, person.lastName)
        // logger.atDebug().addKeyValue("firstName", person.firstName).addKeyValue("lastName", person.lastName).log("Responding with Person:")
        val filter = request.filter.unpack<StringValue>(StringValue::class.java).value
        logger.atDebug().log("filter type: {}", filter)
        while (true) {
            delay(1000)
            emit(
                searchResponse {
                    id = StringValue.of("123e4567-e89b-12d3-a456-426614174000")
                    account = filteredPerson.build()
                }
            )
        }
    }
}
