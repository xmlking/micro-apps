package micro.apps.service.domain.account

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.ExperimentalSerializationApi
import micro.apps.model.Gender
import micro.apps.model.Name
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.redis.connection.stream.ObjectRecord
import org.springframework.data.redis.connection.stream.StreamOffset
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.core.addAndAwait
import org.springframework.data.redis.core.readWithTypeAsFlow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.reactive.TransactionalOperator
import org.springframework.transaction.reactive.executeAndAwait
import java.util.Date

// https://github.com/Taras48/RedisCache/blob/master/src/main/kotlin/com/redis/cache/RedisCache/service/ActorServiceImpl.kt
// TODO: https://todd.ginsberg.com/post/springboot-reactive-kotlin-coroutines/

private val logger = KotlinLogging.logger {}

@OptIn(ExperimentalSerializationApi::class)
@Service()
@Qualifier("reactive")
class RedisReactiveAccountService(
    private val personRepository: PersonRepository,
    private val addressRepository: AddressRepository,
    private val redisTemplate: ReactiveRedisTemplate<String, PersonEntity>,
    private val operator: TransactionalOperator
) : AccountService {
    val hashOperations = redisTemplate.opsForHash<String, PersonEntity>()
    val streamOperations = redisTemplate.opsForStream<String, ChangeEvent>()

    override suspend fun getPerson(id: String): PersonEntity {
        TODO("Not yet implemented")
    }

    override fun getAllPeople(): Flow<PersonEntity> = flow {
        emit(PersonEntity(dob = Date(), gender = Gender.FEMALE, name = Name("sumo", "demo")))
    }

    @Transactional
    override suspend fun updatePerson(id: String, personDto: PersonDto): PersonEntity {
        return PersonEntity(dob = Date(), gender = Gender.FEMALE, name = Name("sumo", "demo"))
    }

    override suspend fun updatePerson(person: PersonEntity): PersonEntity {
        TODO("Not yet implemented")
    }

    suspend fun updatePersonTrans(person: PersonEntity): PersonEntity? = operator.executeAndAwait {
        PersonEntity(dob = Date(), gender = Gender.FEMALE, name = Name("sumo", "demo"))
    }

    override suspend fun createPerson(personDto: PersonDto): PersonEntity {
        return personRepository.save(personDto.toEntity()).also {
            streamOperations.addAndAwait(ObjectRecord.create("events", ChangeEvent(it.id!!, action = Action.CREATED)))
        }
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
