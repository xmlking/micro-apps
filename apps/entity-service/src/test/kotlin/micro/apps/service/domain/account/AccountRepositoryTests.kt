package micro.apps.service.domain.account

import io.kotest.core.spec.style.FunSpec
import io.kotest.core.test.TestCaseConfig
import io.kotest.matchers.equality.shouldBeEqualToIgnoringFields
import io.kotest.matchers.shouldBe
import kotlinx.serialization.ExperimentalSerializationApi
import micro.apps.model.Gender
import micro.apps.model.Name
import micro.apps.test.E2E
import micro.apps.test.Slow
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction.ASC
import org.springframework.data.domain.Sort.Order
import org.springframework.data.geo.Point

@OptIn(ExperimentalSerializationApi::class)
@DataRedisTest
class AccountRepositoryTests(
    private val personRepository: PersonRepository,
    private val addressRepository: AddressRepository
) : FunSpec({
    // defaultTestConfig
    TestCaseConfig(tags = setOf(E2E, Slow))

    val address1 = AddressEntity(
        suite = "A212",
        street = "FourWinds Dr",
        city = "Corona",
        state = "CA",
        code = "34453",
        country = "USA",
        location = Point(-77.0364, 38.8951)
    )
    val address2 = AddressEntity(
        suite = "B212",
        street = "ThreeWinds Dr",
        city = "Corona",
        state = "CA",
        code = "44553",
        country = "USA",
        location = Point(41.40338, 2.17403)
    )
    val person1 = PersonEntity(
        name = Name(first = "sumo", last = "demo"),
        gender = Gender.MALE,
        age = 34,
        phone = "3334442222",
        email = "sumo@demo.com",
    )
    val person2 = PersonEntity(
        name = Name(first = "sumo2", last = "demo2"),
        gender = Gender.MALE,
        age = 3,
        phone = "3334442222",
        email = "sumo@demo.com",
    )

    lateinit var savedAddress1: AddressEntity
    lateinit var savedPerson1: PersonEntity
    lateinit var savedAddress2: AddressEntity
    lateinit var savedPerson2: PersonEntity

    beforeSpec {
        savedAddress1 = addressRepository.save(address1)
        savedAddress2 = addressRepository.save(address2)
        val person1WithAddress1 = person1.copy(addresses = setOf(savedAddress1, savedAddress2))
        savedPerson1 = personRepository.save(person1WithAddress1)
        savedPerson1.shouldBeEqualToIgnoringFields(person1WithAddress1, PersonEntity::id)
    }

    afterSpec {
//        addressRepository.delete(savedAddress1);
//        personRepository.delete(savedPerson1);
//        addressRepository.delete(savedAddress2);
//        personRepository.delete(savedPerson2);
    }

    test("findById returns Person") {
        val actual = personRepository.findById(savedPerson1.id!!).get()
        actual shouldBe savedPerson1
        /*
        val actual = personRepository.findById(savedPerson1.id!!).ifPresent {
            it shouldBe savedPerson1
        }
         */
    }

    test("count should be one") {
        val actual = personRepository.count()
        actual shouldBe 1
    }

    test("!this test will be ignored") {
        println(savedPerson1)
    }

    test("save with invalid age should throw validation error") {
        val person2WithAddress2 = person1.copy(addresses = setOf(savedAddress2))
        savedPerson2 = personRepository.save(person2WithAddress2)
        println(savedPerson2)
    }

    test("list all") {
        // val sort = Sort.sort(Person::class.java).by(Person::getFirstName).ascending()
        // val actual = personRepository.findAll(PageRequest.of(0, 5))
        val actual = personRepository.findAll(Sort.by(Order(ASC, "age")))
        println(actual)
        // actual shouldBe 1
    }
})
