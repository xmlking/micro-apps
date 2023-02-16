package micro.apps.service.bootstrap

import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import micro.apps.model.Gender
import micro.apps.service.domain.account.AccountService
import micro.apps.service.domain.person.Address
import micro.apps.service.domain.person.Name
import micro.apps.service.domain.person.Person
import micro.apps.service.domain.person.PersonRepository
import micro.apps.service.domain.person.mockPersonDto
import micro.apps.service.domain.user.Role
import micro.apps.service.domain.user.RoleRepository
import micro.apps.service.domain.user.User
import micro.apps.service.domain.user.UserRepository
import mu.KotlinLogging
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.data.geo.Point
import org.springframework.stereotype.Component
import java.text.SimpleDateFormat

private val logger = KotlinLogging.logger {}

/**
 * `application.runner.enabled` set to `false` during tests
 */
@Component
@ConditionalOnProperty(
    value = ["application.runner.enabled"],
    havingValue = "true",
    matchIfMissing = true
)
class DataInitializer(
    private val accountService: AccountService,
    private val userRepo: UserRepository,
    private val roleRepo: RoleRepository,
    private val personRepo: PersonRepository
) : ApplicationRunner {

    @OptIn(ExperimentalSerializationApi::class)
    @Throws(Exception::class)
    override fun run(args: ApplicationArguments) {
        logger.info("Your application started with option names : {}", args.optionNames)
        runBlocking {
            val batman = mockPersonDto(mockId = 1)
            val batmanEnt = accountService.createPerson(batman)
            println(batmanEnt)

            val bass = Role(null, roleName = "BASS")
            val vocals = Role(null, roleName = "VOCALS")
            val guitar = Role(null, roleName = "GUITAR")
            val drums = Role(null, roleName = "DRUMS")

            roleRepo.saveAll(listOf(bass, vocals, guitar, drums))

            val john = User(null, "Zack", null, "de la Rocha", "zack@ratm.com", bass)
            val tim = User(null, "Tim", null, "Commerford", "tim@ratm.com", vocals)
            val tom = User(null, "Tom", null, "Morello", "tom@ratm.com", guitar)
            val brad = User(null, "Brad", null, "Wilk", "brad@ratm.com", drums)
            userRepo.saveAll(listOf(john, tim, tom, brad))

            val add1 =
                Address(null, "222", "fourt st", "riverside", "CA", "95543", "USA", Point(-122.124500, 47.640160))
            val add2 = Address(null, "111", "wood", "riverside", "CA", "95543", "USA", Point(-121.124500, 46.640160))
            val add3 = Address(null, "333", "Jambri", "riverside", "CA", "95553", "USA", Point(-111.124500, 44.640160))
            val per1 = Person(
                null,
                0.0,
                Name("kera", "bani", "Mr"),
                setOf(add1, add2),
                add1,
                Gender.MALE,
                SimpleDateFormat("yyyy-MM-dd").parse("1999-05-30"),
                "kera@bani.com"
            )
            val per2 = Person(
                null,
                0.0,
                Name("sumo", "demo", "Sir"),
                setOf(add2, add3),
                add3,
                Gender.FEMALE,
                SimpleDateFormat("yyyy-MM-dd").parse("1989-05-10"),
                "sumo@demo.com"
            )
            personRepo.saveAll(listOf(per1, per2))
        }
    }
}
