package micro.apps.service

import com.redis.om.spring.annotations.Bloom
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Reference
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.index.Indexed

import com.redis.om.spring.annotations.Searchable

@RedisHash
data class Role(
    @Id
    var id: String?,
    @Indexed
    var roleName: String
)

@RedisHash
data class User(
    @Id
    val id: String?,
    @Indexed
    val firstName: String,
    @Indexed
    val middleName: String?,
    @Searchable
    val lastName: String,
    @Indexed
    @Bloom(name = "bf_user_email", capacity = 100000, errorRate = 0.001)
    var email: String,
    @Reference
    val role: Role
)

