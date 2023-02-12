package micro.apps.service.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("security.users")
data class UsersProperties(
    val basicAuth: List<BasicAuthUser>
) {
    data class BasicAuthUser(
        val username: String,
        val password: String,
        val authorities: Set<String>
    )
}
