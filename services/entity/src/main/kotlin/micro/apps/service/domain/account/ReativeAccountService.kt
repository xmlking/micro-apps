package micro.apps.service.domain.account

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.ExperimentalSerializationApi
import micro.apps.model.Gender
import micro.apps.model.Name
import micro.apps.service.RecordNotFoundException
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.redis.connection.stream.StreamOffset
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.core.getAndAwait
import org.springframework.data.redis.core.readWithTypeAsFlow
import org.springframework.data.redis.core.valuesAsFlow
import org.springframework.stereotype.Service
import java.util.Date

// https://github.com/Taras48/RedisCache/blob/master/src/main/kotlin/com/redis/cache/RedisCache/service/ActorServiceImpl.kt
// TODO: https://todd.ginsberg.com/post/springboot-reactive-kotlin-coroutines/

private val logger = KotlinLogging.logger {}

@OptIn(ExperimentalSerializationApi::class)
@Service()
@Qualifier("reactive")
class RedisReactiveAccountService(redisTemplate: ReactiveRedisTemplate<String, PersonEntity>) : AccountService {
    val hashOperations = redisTemplate.opsForHash<String, PersonEntity>()
    val streamOperations = redisTemplate.opsForStream<String, ChangeEvent>()

    override suspend fun getPerson(id: String): PersonEntity =
        hashOperations.getAndAwait("people", id) ?: throw RecordNotFoundException("Person with id - $id not found")

    override fun getAllPeople(): Flow<PersonEntity> = hashOperations.valuesAsFlow("people")

    override suspend fun updatePerson(id: String, personDto: PersonDto): PersonEntity {
        return PersonEntity(dob = Date(), gender = Gender.FEMALE, name = Name("sumo", "demo"))
    }

    override suspend fun updatePerson(person: PersonEntity): PersonEntity {
        TODO("Not yet implemented")
    }

    suspend fun updatePersonTrans(person: PersonEntity): PersonEntity {
        return PersonEntity(dob = Date(), gender = Gender.FEMALE, name = Name("sumo", "demo"))
    }

    override suspend fun createPerson(personDto: PersonDto): PersonEntity {
        TODO("Not yet implemented")
    }

    override fun events() = streamOperations
        .readWithTypeAsFlow<String, ChangeEvent>(StreamOffset.fromStart("events")).map { it.value }

    override suspend fun deletePerson(id: String) {
        TODO("Not yet implemented")
    }

    override suspend fun addAddressToPerson(addressId: String, personId: String): PersonEntity {
        TODO("Not yet implemented")
    }
}
