package micro.apps.service.domain.book

data class CreateBookInput(
    val title: String,
    val author: String,
    val isbn: String
)
