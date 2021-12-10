package micro.apps.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.Optional

@RestController
@RequestMapping("/api/users")
class UserController(private val userRepository: UserRepository) {
//    @Autowired
//    private val userRepository: UserRepository? = null
    @PostMapping("/")
    fun save(@RequestBody user: User): User {
        return userRepository.save(user)
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
