package micro.apps.service

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
