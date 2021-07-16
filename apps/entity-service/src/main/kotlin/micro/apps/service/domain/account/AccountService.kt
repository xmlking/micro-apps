package micro.apps.service.domain.account

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.serialization.ExperimentalSerializationApi
import micro.apps.model.AddressNotFoundException
import micro.apps.model.Gender
import micro.apps.model.Name
import micro.apps.model.PersonNotFoundException
import mu.KotlinLogging
import org.springframework.stereotype.Service

// https://github.com/Taras48/RedisCache/blob/master/src/main/kotlin/com/redis/cache/RedisCache/service/ActorServiceImpl.kt

@OptIn(ExperimentalSerializationApi::class)
interface AccountService {
    @Throws(PersonNotFoundException::class)
    suspend fun getPerson(id: String): PersonEntity

    suspend fun getAllPeople(): Flow<PersonEntity>

    @Throws(PersonNotFoundException::class)
    suspend fun updatePerson(id: String, personDto: PersonDto): PersonEntity

    suspend fun updatePerson(person: PersonEntity): PersonEntity

    suspend fun createPerson(personDto: PersonDto): PersonEntity

    @Throws(PersonNotFoundException::class)
    suspend fun deletePerson(id: String)

    @Throws(AddressNotFoundException::class, PersonNotFoundException::class)
    suspend fun addAddressToPerson(addressId: String, personId: String): PersonEntity
}

private val logger = KotlinLogging.logger {}

@OptIn(ExperimentalSerializationApi::class)
@Service
class RedisAccountService(
    private val personRepository: PersonRepository,
    private val addressRepository: AddressRepository
) : AccountService {

    override suspend fun getPerson(id: String): PersonEntity =
        personRepository.findById(id).orElseThrow { PersonNotFoundException("Person with id - $id not found") }

    override suspend fun getAllPeople(): Flow<PersonEntity> = personRepository.findAll().asFlow()

    suspend fun findAllAdults(): Flow<PersonEntity> {
        return personRepository.findAll().filter {
            it.age > 18
        }.asFlow()
    }

    override suspend fun updatePerson(id: String, personDto: PersonDto): PersonEntity {
        logger.atDebug().addKeyValue("id", id).log("service updatePerson")
        
        val actor =
            personRepository.findById(id).orElseThrow { PersonNotFoundException("Person with id - $id not found") }
        val upActor = actor.copy(email = personDto.email)
        return personRepository.save(upActor)
    }

    override suspend fun updatePerson(person: PersonEntity): PersonEntity = personRepository.save(person)


    override suspend fun createPerson(personDto: PersonDto): PersonEntity =

        personRepository.save(
            PersonEntity(
                name = Name(first = personDto.username, last = personDto.password),
                email = personDto.email,
                age = 0,
                gender = Gender.FEMALE,
                phone = "1112223344"
            )
        )


    override suspend fun deletePerson(id: String) = personRepository.delete(getPerson(id))


    override suspend fun addAddressToPerson(addressId: String, personId: String): PersonEntity {
        // Run 2 findById parallel
        val person: PersonEntity = personRepository.findById(personId).orElseThrow {
            PersonNotFoundException("Unable to find person for $personId id")
        }
        val address: AddressEntity = addressRepository.findById(addressId).orElseThrow {
            AddressNotFoundException("Unable to find address for $addressId id")
        }
        (person.addresses as HashSet).add(address)
        return updatePerson(person)
    }

}

/*
@OptIn(ExperimentalSerializationApi::class)
@Service
class RedisAccountService2(
    private val personRepository: PersonRepository,
    private val addressRepository: AddressRepository
    ) : CrudService<PersonEntity, PersonDto> {
    override suspend fun get(id: String): PersonEntity {
        TODO("Not yet implemented")
    }

    override suspend fun getAll(): Flow<PersonEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun update(id: String, dto: PersonDto): PersonEntity {
        TODO("Not yet implemented")
    }

    override suspend fun update(obg: PersonEntity): PersonEntity {
        TODO("Not yet implemented")
    }

    override suspend fun create(obg: PersonDto): PersonEntity {
        TODO("Not yet implemented")
    }

    override suspend fun delete(id: String) {
        TODO("Not yet implemented")
    }

}
*/
