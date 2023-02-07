package micro.apps.service

import com.redis.om.spring.annotations.Bloom
import com.redis.om.spring.annotations.Indexed
import com.redis.om.spring.annotations.Searchable
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Reference
import org.springframework.data.redis.core.RedisHash

@RedisHash
data class Role(
    @Id
    var id: String?,
    @Indexed
    var roleName: String?
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
    // HINT: Reference fields should be marked as nullable.???
    @Reference
    val role: Role?
)
