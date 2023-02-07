package micro.apps.service

import com.google.gson.Gson
import com.redis.om.spring.ops.RedisModulesOperations
import io.redisearch.Document
import io.redisearch.Query
import io.redisearch.SearchResult
import kotlinx.serialization.ExperimentalSerializationApi
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*

@OptIn(ExperimentalSerializationApi::class)
@RestController
@RequestMapping("/api/people")
class PersonController(private val personRepository: PersonRepository, private val modulesOperations: RedisModulesOperations<String>) {
    var ops = modulesOperations.opsForSearch("PersonIdx")
    private val gson: Gson = Gson()

    @GetMapping("search/{q}")
    fun fullTextSearch(@PathVariable("q") q: String): List<Person> {
        return personRepository.search(q)
    }

    @GetMapping("title")
    fun findByTitle(@RequestParam title: String): Person? {
        val result: SearchResult = ops.search(Query("@address_city:'$title'"))
        if (result.totalResults > 0) {
            val doc: Document = result.docs[0]
            return gson.fromJson(doc.toString(), Person::class.java)
        } else return null
    }

//    @PostMapping("/")
//    fun save(@RequestBody user: User): User {
//        return userRepository.save(user)
//    }
//
//    @GetMapping("/q")
//    fun findByName(@RequestParam firstName: String, @RequestParam lastName: String): List<User> {
//        return userRepository.findByFirstNameAndLastName(firstName, lastName)
//    }
//
//    @GetMapping("name/{lastName}")
//    fun byName(@PathVariable("lastName") lastName: String): Optional<User> {
//        return userRepository.findOneByLastName(lastName)
//    }
//
//    @GetMapping("start/{lastName}")
//    fun byNameStart(@PathVariable("lastName") lastName: String): List<User> {
//        return userRepository.findByLastNameStartsWithIgnoreCase(lastName)
//    }
//
//    @GetMapping("/exists/")
//    fun isEmailTaken(@RequestParam("email") email: String): Boolean {
//        return userRepository.existsByEmail(email)
//    }
}
