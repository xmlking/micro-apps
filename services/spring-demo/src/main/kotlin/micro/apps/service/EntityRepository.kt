package micro.apps.service

import io.opentelemetry.api.trace.Span
import io.opentelemetry.instrumentation.annotations.WithSpan
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.ExperimentalSerializationApi
import micro.apps.model.Address
import micro.apps.model.Gender
import micro.apps.model.Name
import micro.apps.model.Person
import mu.KotlinLogging
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@OptIn(ExperimentalSerializationApi::class)
interface EntityRepository {
    suspend fun all(): Flow<Person>
    suspend fun get(id: String): Person?
    suspend fun add(customer: Person): Result<Unit>
}

private val logger = KotlinLogging.logger {}

@Repository
@OptIn(ExperimentalSerializationApi::class)
class RedisEntityRepository() : EntityRepository {
    override suspend fun all(): Flow<Person> {
        TODO("Not yet implemented")
    }

    @WithSpan
    override suspend fun get(id: String): Person? {
        val span = Span.current()
        logger.atInfo().addKeyValue("traceId", span.spanContext.traceId).log("traceId")
        span.setAttribute("attribute.a2", "some value")

        return Person(
            name = Name(first = "sumo1", last = "demo1"),
            addresses = setOf(
                Address(
                    suite = "1234",
                    street = "Wood Road",
                    city = "Riverside",
                    state = "California",
                    code = "92505",
                    country = "CA"
                )
            ),
            dob = LocalDateTime.of(2013, 12, 18, 14, 30),
            gender = Gender.MALE,
            age = 99,
            email = "sumo1@demo.com",
            phone = "0000000000"
        )
    }

    override suspend fun add(customer: Person): Result<Unit> {
        TODO("Not yet implemented")
    }
}
