package micro.apps.service.domain.account

import io.kotest.core.spec.style.FunSpec
import io.kotest.core.test.TestCaseConfig
import io.kotest.matchers.collections.shouldExistInOrder
import io.kotest.matchers.equality.shouldBeEqualToIgnoringFields
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.delay
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import micro.apps.core.dateOf
import micro.apps.test.E2E
import micro.apps.test.Slow
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction.ASC
import org.springframework.data.domain.Sort.Order

@OptIn(ExperimentalSerializationApi::class)
@DataRedisTest
class AccountRepositoryTests(
    private val personRepository: PersonRepository,
    private val addressRepository: AddressRepository
) : FunSpec({
    // defaultTestConfig
    TestCaseConfig(tags = setOf(E2E, Slow))

    val aPersonId = "1073516001"
    lateinit var aPerson: PersonEntity

    beforeSpec {
        println("***DID YOU RUN `FLUSHDB` REDIS COMMAND TO CLEAN THE DATABASE?")

        javaClass.getResourceAsStream("/npidata.jsonl").use {
            it?.bufferedReader()?.forEachLine { line ->
                val person = Json.decodeFromString(PersonEntity.serializer(), line)
                val savedAddresses: MutableSet<AddressEntity> = mutableSetOf()
                person.addresses?.forEach {
                    savedAddresses += addressRepository.save(it)
                }
                val personWithSavedAddresses = person.copy(addresses = savedAddresses)
                personRepository.save(personWithSavedAddresses)
            }
            delay(1000L)
            aPerson = personRepository.findById(aPersonId).get()
        }
    }

    afterSpec {
        addressRepository.deleteAll()
        personRepository.deleteAll()
    }

    test("findById returns Person") {
        val actual = personRepository.findById(aPersonId).get()
        actual shouldBe aPerson
        /*
        val actual = personRepository.findById(aPersonId).ifPresent {
            it shouldBe aPerson
        }
         */
    }

    test("count should be one") {
        val actual = personRepository.count()
        actual shouldBe 100
    }

    test("!this test will be ignored") {
        println(aPerson)
    }

    test("update aPerson's dob and email") {
        val savedAPersonUpdated =
            personRepository.save(aPerson.copy(dob = dateOf(1965, 8, 26), email = "me2@demo.com"))
        savedAPersonUpdated.id shouldBe aPerson.id
        savedAPersonUpdated.dob shouldBe dateOf(1965, 8, 26)
        savedAPersonUpdated.email shouldBe "me2@demo.com"
        savedAPersonUpdated.shouldBeEqualToIgnoringFields(aPerson, PersonEntity::dob, PersonEntity::email)
    }

    test("list all") {
        // val sort = Sort.sort(Person::class.java).by(Person::getFirstName).ascending()
        // val sorted = personRepository.findAll(PageRequest.of(0, 5))
        val sorted = personRepository.findAll(Sort.by(Order(ASC, "dob")))
        sorted shouldExistInOrder listOf { it.id != null }
    }
})
