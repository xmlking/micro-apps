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

    override suspend fun getPerson(id: String): PersonEntity =
        personRepository.findById(id).orElseThrow { RecordNotFoundException("Person with id - $id not found") }

    override suspend fun getAllPeople(): Flow<PersonEntity> = personRepository.findAll().asFlow()

    suspend fun findAllAdults(): Flow<PersonEntity> {
        return personRepository.findAll().filter {
            it.age?.let { it >= 18 } ?: false
        }.asFlow()
    }

    override suspend fun updatePerson(id: String, personDto: PersonDto): PersonEntity {
        logger.atDebug().addKeyValue("id", id).log("service updatePerson")

        val person = getPerson(id)

        // do deep non-null copy person <-- personDto
        // TODO: wish `with(person)` or `person.apply {}` works
        val upActor = person.copy(
            name = personDto.name?.let {
                person.name?.copy(
                    first = personDto.name.first ?: person.name.first,
                    last = personDto.name.last ?: person.name.last,
                    title = personDto.name.title ?: person.name.title
                )
            } ?: person.name,
            addresses = personDto.addresses?.let { it -> it.map { it.toEntity() } }?.toSet() ?: person.addresses,
            gender = personDto.gender ?: person.gender,
            age = personDto.age ?: person.age,
            email = personDto.email ?: person.email,
            phone = personDto.phone ?: person.phone,
        )
        return updatePerson(upActor)
    }

    @Transactional
    override suspend fun updatePerson(person: PersonEntity): PersonEntity {
        person.addresses?.forEach {
            addressRepository.save(it)
        }
        return personRepository.save(person)
    }

    @Transactional
    override suspend fun createPerson(personDto: PersonDto): PersonEntity {
        val addresses = personDto.addresses?.map { it.toEntity() }?.map { addressRepository.save(it) }?.toSet()
        return personRepository.save(
            PersonEntity(
                null,
                personDto.name,
                addresses,
                personDto.gender,
                personDto.age,
                personDto.email,
                personDto.phone,
                personDto.avatar
            )
        )
    }

    @Transactional
    override suspend fun deletePerson(id: String) {
        val person = getPerson(id)
        person.addresses?.forEach {
            addressRepository.delete(it)
        }
        personRepository.delete(person)
    }

    @Transactional
    override suspend fun addAddressToPerson(addressId: String, personId: String): PersonEntity {
        // Run 2 findById parallel
        val person: PersonEntity = personRepository.findById(personId).orElseThrow {
            RecordNotFoundException("Unable to find person for $personId id")
        }
        val address: AddressEntity = addressRepository.findById(addressId).orElseThrow {
            RecordNotFoundException("Unable to find address for $addressId id")
        }
        (person.addresses as HashSet).add(address)
        return updatePerson(person)
    }

    // suspend fun addAddressToPerson2(addressId: String, personId: String): PersonEntity = coroutineScope {
    suspend fun addAddressToPerson2(addressId: String, personId: String): PersonEntity = withContext(Dispatchers.IO) {
        lateinit var person: PersonEntity
        lateinit var address: AddressEntity
        awaitAll(
            async {
                person = personRepository.findById(personId).orElseThrow {
                    RecordNotFoundException("Unable to find person for $personId id")
                }
            },
            async {
                address = addressRepository.findById(addressId).orElseThrow {
                    RecordNotFoundException("Unable to find address for $addressId id")
                }
            }
        )
        ((person).addresses as HashSet).add(address)
        updatePerson(person)
    }
}
