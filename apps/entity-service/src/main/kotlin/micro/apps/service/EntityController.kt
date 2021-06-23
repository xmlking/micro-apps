package micro.apps.service

import kotlinx.coroutines.delay
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

data class User(val id: String)

@CrossOrigin
@RestController
class EntityController {

    @GetMapping("/")
    suspend fun hello(): String {
        return "Hello"
    }

    @GetMapping("/slow")
    suspend fun findOne(id: String): User {
        delay(2000)
        return User(id)
    }
}
