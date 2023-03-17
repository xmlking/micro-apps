package micro.apps.service.domain.book

import com.github.f4b6a3.uuid.UuidCreator
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
    // setting id = null, lets database set UUID if supported.
    // @Id val id: UUID? = null,
    /**
     * Having application generated ids means the id is known even before the entity is persisted.
     * This lets us model our entities as immutable objects and we avoid having to handle null values on the id
     * https://jivimberg.io/blog/2018/11/05/using-uuid-on-spring-data-jpa-entities/
     */
    @Id val id: UUID = UuidCreator.getTimeOrderedEpoch(),
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
    @Id val id: UUID = UuidCreator.getTimeOrderedEpoch(),
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
