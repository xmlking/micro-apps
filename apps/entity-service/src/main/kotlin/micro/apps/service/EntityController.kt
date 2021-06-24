package micro.apps.service

import kotlinx.coroutines.delay
import kotlinx.serialization.ExperimentalSerializationApi
import micro.apps.model.Person
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@ExperimentalSerializationApi
@CrossOrigin
@RestController
class EntityController(private val repository: EntityRepository) {
    @GetMapping("/entity/{id}")
    suspend fun findOne(@PathVariable("id") id: String): Person? {
        delay(2000)
        return repository.get(id)
    }

    @GetMapping("/intro")
    suspend fun hello(): String {
        return "Hello"
    }
}
