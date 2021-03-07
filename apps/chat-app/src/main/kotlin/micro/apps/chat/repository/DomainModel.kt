package micro.apps.chat.repository

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table("MESSAGES")
data class Message(
    val content: String,
    val contentType: ContentType,
    val sent: Instant,
    val username: String,
    val userAvatarImageLink: String,
    @Id var id: String? = null
)

enum class ContentType {
    PLAIN, MARKDOWN
}
