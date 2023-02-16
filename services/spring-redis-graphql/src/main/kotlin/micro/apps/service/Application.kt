package micro.apps.service

import com.redis.om.spring.annotations.EnableRedisEnhancedRepositories
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@ConfigurationPropertiesScan
@SpringBootApplication
@EnableRedisEnhancedRepositories(basePackages = ["micro.apps.service"], considerNestedRepositories = false) // HINT without basePackages, index will not be created
class RomsHashesApplication

fun main(args: Array<String>) {
    runApplication<RomsHashesApplication>(*args)
}
