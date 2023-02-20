package micro.apps.service.domain.book

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.util.UUID

@Table("books")
data class Book(
    @Id val id: UUID,
    val title: String,
    val pages: Int,
    val category: Category,
    @Version
    val version: Int = 0,
    @Column("created_at")
    @CreatedDate
    var createdAt: LocalDateTime = LocalDateTime.now(),
    @LastModifiedDate
    @Column("last_modified_at")
    var lastModifiedAt: LocalDateTime? = LocalDateTime.now()
)

enum class Category {
    HORROR, COMEDY, FANTASY
}

@Table("authors")
data class Author(
    @Id val id: UUID,
    val name: String,
    val age: Int?,
    @Version
    val version: Int = 0,
    @Column("book_id")
    private val bookId: UUID? = null
)

data class CreateBookInput(
    val title: String,
    val pages: Int,
    val category: Category,
    val author: String
)
