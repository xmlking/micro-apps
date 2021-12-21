package micro.apps.service

import com.redis.om.spring.annotations.EnableRedisDocumentRepositories
import org.springframework.boot.autoconfigure.SpringBootApplication
import com.redis.om.spring.annotations.EnableRedisEnhancedRepositories
import kotlinx.serialization.ExperimentalSerializationApi
import micro.apps.model.Gender
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.geo.Point
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField

@SpringBootApplication
@EnableRedisEnhancedRepositories(basePackages = ["micro.apps.service"]) // HINT without basePackages, index will not be created
@EnableRedisDocumentRepositories(basePackages = ["micro.apps.service"] , considerNestedRepositories= true)
class RomsHashesApplication {
    @Autowired
    private lateinit var userRepo: UserRepository

    @Autowired
    private lateinit var roleRepo: RoleRepository

    @Autowired
    private lateinit var personRepo: PersonRepository

    @OptIn(ExperimentalSerializationApi::class)
    @Bean
    fun loadTestData(): CommandLineRunner {
        return CommandLineRunner { args: Array<String> ->
            val bass = Role(null, roleName = "BASS")
            val vocals = Role(null, roleName ="VOCALS")
            val guitar = Role(null, roleName ="GUITAR")
            val drums = Role(null, roleName ="DRUMS")

            //TODO: handle @Reference deserialization
            //roleRepo.saveAll(List.of(bass, vocals, guitar, drums));

            val john = User(null, "Zack", null, "de la Rocha", "zack@ratm.com", bass)
            val tim = User(null, "Tim", null, "Commerford", "tim@ratm.com", vocals)
            val tom = User(null, "Tom", null,"Morello", "tom@ratm.com", guitar)
            val brad = User(null, "Brad", null,"Wilk", "brad@ratm.com", drums)
            userRepo.saveAll(listOf(john, tim, tom, brad))

            val add1 = Address(null, "222", "fourt st", "riverside", "CA", "95543", "USA",  Point(-122.124500, 47.640160))
            val per1 = Person(null, Name("sumo", "demo", "Sr"),  setOf(add1), Gender.MALE, SimpleDateFormat("yyyy-MM-dd").parse("1999-05-30"), "sumo@demo.com")
            personRepo.saveAll(listOf(per1))
        }
    }
}

fun main(args: Array<String>) {
    runApplication<RomsHashesApplication>(*args)
}
