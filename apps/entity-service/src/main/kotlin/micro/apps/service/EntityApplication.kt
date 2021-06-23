package micro.apps.service

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class EntityApplication

fun main(args: Array<String>) {
    runApplication<EntityApplication>(*args)
}
