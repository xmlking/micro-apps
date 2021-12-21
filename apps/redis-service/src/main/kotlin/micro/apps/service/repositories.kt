package micro.apps.service

 import com.redis.om.spring.repository.RedisDocumentRepository
 import micro.apps.model.Gender
 import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : CrudRepository<User, String> {
    fun findOneByLastName(lastName: String): Optional<User>
    fun findByFirstNameAndLastName(firstName: String, lastName: String): List<User>
    fun findByLastNameStartsWithIgnoreCase(lastName: String): List<User>
    fun existsByEmail(email: String): Boolean
}

@Repository
interface RoleRepository : CrudRepository<Role, String>

@Repository
interface PersonRepository : RedisDocumentRepository<Person, String> {
    fun findByNameStartsWithIgnoreCase(name: String): List<Person>
    fun findByGender(gender: Gender): List<Person>
    fun existsByEmail(email: String): Boolean
}
