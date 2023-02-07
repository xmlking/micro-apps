package micro.apps.service.domain.book

import lombok.NoArgsConstructor
import lombok.RequiredArgsConstructor
import org.springframework.data.annotation.Id

@RequiredArgsConstructor
@NoArgsConstructor
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
