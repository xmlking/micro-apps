package micro.apps.service.domain.account

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import micro.apps.service.RecordNotFoundException
import micro.apps.service.config.ChangeEventProperties
import mu.KotlinLogging
import org.springframework.context.annotation.Primary
import org.springframework.data.redis.connection.stream.ObjectRecord
import org.springframework.data.redis.connection.stream.StreamOffset
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.core.addAndAwait
import org.springframework.data.redis.core.readWithTypeAsFlow
import org.springframework.stereotype.Service
import java.util.Calendar

// https://github.com/Taras48/RedisCache/blob/master/src/main/kotlin/com/redis/cache/RedisCache/service/ActorServiceImpl.kt

@OptIn(ExperimentalSerializationApi::class)
interface AccountService {
    @Throws(RecordNotFoundException::class)
    suspend fun getPerson(id: String): PersonEntity

    fun getAllPeople(): Flow<PersonEntity>

    @Throws(RecordNotFoundException::class)
    suspend fun updatePerson(id: String, personDto: PersonDto): PersonEntity

    suspend fun updatePerson(person: PersonEntity): PersonEntity

    suspend fun createPerson(personDto: PersonDto): PersonEntity

    @Throws(RecordNotFoundException::class)
    suspend fun deletePerson(id: String)

    @Throws(RecordNotFoundException::class)
    suspend fun addAddressToPerson(addressId: String, personId: String): PersonEntity

    fun events(): Flow<ChangeEvent>
}

private val logger = KotlinLogging.logger {}

@OptIn(ExperimentalSerializationApi::class)
@Service
@Primary
class RedisAccountService(
    private val personRepository: PersonRepository,
    private val addressRepository: AddressRepository,
    private val redisTemplate: ReactiveRedisTemplate<String, PersonEntity>,
    private val ceProps: ChangeEventProperties,
) : AccountService {
    //  val (ceEnabled, ecKeyspace)  = ceProps
    val ceEnabled  = ceProps.enabled
    val ecKeyspace = ceProps.keyspace

    val hashOperations = redisTemplate.opsForHash<String, PersonEntity>()
    val streamOperations = redisTemplate.opsForStream<String, ChangeEvent>()

    override suspend fun getPerson(id: String): PersonEntity =
        personRepository.findById(id).orElseThrow { RecordNotFoundException("Person with id - $id not found") }

    override fun getAllPeople(): Flow<PersonEntity> = personRepository.findAll().asFlow()

    suspend fun findAllAdults(): Flow<PersonEntity> {
        return personRepository.findAll().filter {
            val today = Calendar.getInstance()
            it.dob?.let { today[Calendar.YEAR] - it.year > 18 } ?: false
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
            dob = personDto.dob ?: person.dob,
            email = personDto.email ?: person.email,
            phone = personDto.phone ?: person.phone,
        )
        return updatePerson(upActor)
    }

    override suspend fun updatePerson(person: PersonEntity): PersonEntity {
        logger.atDebug().log("saving person")
        val savedAddresses = person.addresses?.map {
            addressRepository.save(it)
                .also { publishChangeEvent(ADDRESS, it.id, Action.UPDATED) }
        }?.toSet()

        if (savedAddresses != null) {
            return personRepository.save(person.copy(addresses = savedAddresses))
                .also { publishChangeEvent(PEOPLE, it.id, Action.UPDATED) }
        } else {
            return personRepository.save(person)
                .also { publishChangeEvent(PEOPLE, it.id, Action.UPDATED) }
        }
    }

    override suspend fun createPerson(personDto: PersonDto): PersonEntity {
        val addresses = personDto.addresses?.map { it.toEntity() }?.map {
            addressRepository.save(it)
                .also { publishChangeEvent(ADDRESS, it.id, Action.CREATED) }
        }?.toSet()

        return personRepository.save(
            PersonEntity(
                null,
                personDto.name,
                addresses,
                personDto.gender,
                personDto.dob,
                personDto.email,
                personDto.phone,
                personDto.avatar
            )
        )
            .also { publishChangeEvent(PEOPLE, it.id, Action.CREATED) }
    }

    override suspend fun deletePerson(id: String) {
        val person = getPerson(id)
        person.addresses?.forEach { address ->
            addressRepository.delete(address)
                .also { publishChangeEvent(ADDRESS, address.id, Action.DELETED) }
        }
        personRepository.delete(person)
            .also { publishChangeEvent(PEOPLE, id, Action.DELETED) }
    }

    // override suspend fun addAddressToPerson(addressId: String, personId: String): PersonEntity = coroutineScope {
    override suspend fun addAddressToPerson(addressId: String, personId: String): PersonEntity = withContext(Dispatchers.IO) {
        // val person: PersonEntity = personRepository.findById(personId).orElseThrow {
        //     RecordNotFoundException("Unable to find person for $personId id")
        // }
        // val address: AddressEntity = addressRepository.findById(addressId).orElseThrow {
        //     RecordNotFoundException("Unable to find address for $addressId id")
        // }

        // HINT: awaitAll() cancel all other jobs as-soon-as, if any one of the jobs fail
        val (person, address) = awaitAll(
            async {
                logger.atDebug().log("getting person async")
                personRepository.findById(personId).orElseThrow {
                    RecordNotFoundException("Unable to find person for $personId id")
                }
            },
            async {
                logger.atDebug().log("getting address async")
                addressRepository.findById(addressId).orElseThrow {
                    RecordNotFoundException("Unable to find address for $addressId id")
                }
            }
        )

        ((person as PersonEntity).addresses as HashSet).add(address as AddressEntity)
        personRepository.save(person)
            .also { publishChangeEvent(PEOPLE, personId, Action.UPDATED) }
    }

    override fun events() = streamOperations
        .readWithTypeAsFlow<String, ChangeEvent>(StreamOffset.fromStart(EVENTS))
        // .onEach { logger.atDebug().addKeyValue("event", it).log("Received stream record:") }
        .map { it.value }

    private suspend fun publishChangeEvent(prefix: String, id: String?, action: Action) {
        if (ceEnabled) {
            streamOperations.addAndAwait(ObjectRecord.create(ecKeyspace, ChangeEvent("$prefix:$id", action = action)))
        }
    }

    companion object {
        private const val ADDRESS = "address"
        private const val PEOPLE = "people"
        private const val EVENTS = "events"
    }
}
