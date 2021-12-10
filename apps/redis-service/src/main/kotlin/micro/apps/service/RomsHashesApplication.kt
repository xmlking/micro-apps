package micro.apps.service

import org.springframework.boot.autoconfigure.SpringBootApplication
import com.redis.om.spring.annotations.EnableRedisEnhancedRepositories
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
@EnableRedisEnhancedRepositories(basePackages = ["micro.apps.service"]) // HINT without basePackages, index will not be created
class RomsHashesApplication {
    @Autowired
    private lateinit var userRepo: UserRepository

    @Autowired
    private lateinit var roleRepo: RoleRepository

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
        }
    }
}

fun main(args: Array<String>) {
    runApplication<RomsHashesApplication>(*args)
}
