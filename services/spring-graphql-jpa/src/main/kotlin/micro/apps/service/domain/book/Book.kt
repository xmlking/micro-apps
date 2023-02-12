package micro.apps.service.domain.book

import org.springframework.data.annotation.Id
// import micro.apps.model.NoArg
// @NoArg
data class Book(
    val title: String,
    val author: String,
    val isbn: String,
    val publishers: List<Publisher> = emptyList(),
    val info: Info? = null
) {
    @Id
    lateinit var id: String
}

data class Publisher(
    val name: String,
    val email: String
)

data class Info(
    val preface: String
)
