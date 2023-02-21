package micro.apps.service.domain.book

import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.util.UUID

@Table("books")
data class Book(
    @Id val id: UUID? = null,
    val title: String,
    val pages: Int,
    val category: Category,

    // Audit fields
    @Column("created_at")
    @CreatedDate
    var createdAt: LocalDateTime? = null, // LocalDateTime.now(),
    @Column("created_by")
    @CreatedBy
    private val createdBy: String? = null,
    @Column("updated_at")
    @LastModifiedDate
    var updatedAt: LocalDateTime? = null, // LocalDateTime.now(),
    @Column("updated_by")
    @LastModifiedBy
    private val updatedBy: String? = null,
    @Column("version")
    @Version
    val version: Int = 0
)

enum class Category {
    HORROR, COMEDY, FANTASY
}

@Table("authors")
data class Author(
    @Id val id: UUID? = null,
    val name: String,
    val age: Int?,
    @Column("book_id")
    private val bookId: UUID? = null,
    @Version
    val version: Int = 0
)

data class CreateBookInput(
    val title: String,
    val pages: Int,
    val category: Category,
    val author: String
)
