package micro.apps.service.domain.book

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

// import org.springframework.data.r2dbc.repository.Query

@Table
data class Book(
    val title: String,
    val author: String,
    val isbn: String,
    val publishers: List<Publisher> = emptyList(),
    val info: Info? = null,
    @Column("created_at")
    @CreatedDate
    var createdAt: LocalDateTime = LocalDateTime.now(),
    @LastModifiedDate
    @Column("last_modified_at")
    var lastModifiedAt: LocalDateTime? = LocalDateTime.now()
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
