package micro.apps.service

import com.google.gson.Gson
import com.redis.om.spring.ops.RedisModulesOperations
import io.redisearch.Document
import io.redisearch.Query
import io.redisearch.SearchResult
import kotlinx.serialization.ExperimentalSerializationApi
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.Optional

@OptIn(ExperimentalSerializationApi::class)
@RestController
@RequestMapping("/api/users")
class UserController(private val userRepository: UserRepository, private val modulesOperations: RedisModulesOperations<String, String>) {
    var ops = modulesOperations.opsForSearch("PersonIdx")
    private val gson: Gson = Gson()

    @PostMapping("/")
    fun save(@RequestBody user: User): User {
        return userRepository.save(user)
    }

    @GetMapping("/title")
    fun findByTitle(@RequestParam title: String): Person? {
        val result: SearchResult = ops.search(Query("@title:'$title'"))
        if (result.totalResults > 0) {
            val doc: Document = result.docs[0]
            return gson.fromJson(doc.toString(), Person::class.java)
        } else return null
    }

    @GetMapping("/q")
    fun findByName(@RequestParam firstName: String, @RequestParam lastName: String): List<User> {
        return userRepository.findByFirstNameAndLastName(firstName, lastName)
    }

    @GetMapping("name/{lastName}")
    fun byName(@PathVariable("lastName") lastName: String): Optional<User> {
        return userRepository.findOneByLastName(lastName)
    }

    @GetMapping("start/{lastName}")
    fun byNameStart(@PathVariable("lastName") lastName: String): List<User> {
        return userRepository.findByLastNameStartsWithIgnoreCase(lastName)
    }

    @GetMapping("/exists/")
    fun isEmailTaken(@RequestParam("email") email: String): Boolean {
        return userRepository.existsByEmail(email)
    }
}
