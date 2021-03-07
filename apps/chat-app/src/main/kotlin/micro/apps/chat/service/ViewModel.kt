package micro.apps.chat.service

import java.net.URL
import java.time.Instant

data class MessageVM(val content: String, val user: UserVM, val sent: Instant, val id: String? = null)

data class UserVM(val name: String, val avatarImageLink: URL)
