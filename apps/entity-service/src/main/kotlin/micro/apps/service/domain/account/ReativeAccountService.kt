package micro.apps.service.domain.account

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import micro.apps.service.RecordNotFoundException
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

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
) : AccountService {
    val hashOperations = redisTemplate.opsForHash<String, PersonEntity>()
    override suspend fun getPerson(id: String): PersonEntity {
        TODO("Not yet implemented")
    }

    override suspend fun getAllPeople(): Flow<PersonEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun updatePerson(id: String, personDto: PersonDto): PersonEntity {
        TODO("Not yet implemented")
    }

    override suspend fun updatePerson(person: PersonEntity): PersonEntity {
        TODO("Not yet implemented")
    }

    override suspend fun createPerson(personDto: PersonDto): PersonEntity {
        TODO("Not yet implemented")
    }

    override suspend fun deletePerson(id: String) {
        TODO("Not yet implemented")
    }

    override suspend fun addAddressToPerson(addressId: String, personId: String): PersonEntity {
        TODO("Not yet implemented")
    }

}
