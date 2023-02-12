package micro.apps.service.domain.item

import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import kotlinx.coroutines.flow.Flow
import mu.KotlinLogging
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.kotlin.CoroutineSortingRepository
import org.springframework.graphql.data.GraphQlRepository
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Transactional

// import org.springframework.data.r2dbc.repository.Query

@Table
data class Item(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val name: String,
    val description: String?
)

data class ItemInput(
    val name: String,
    val description: String?
) {
    fun toEntity() = Item(name = this.name, description = this.description)
}

@GraphQlRepository
interface ItemRepository : CoroutineCrudRepository<Item, Long>, CoroutineSortingRepository<Item, Long> {
//    @Query("select m from Message m where m.to.id = ?#{ principal?.id }")
//    fun findInbox(pageable: Pageable?): Page<Message?>?
//    suspend fun findOne(id: String): Item
//    fun findByFirstname(firstname: String): Flow<Item>
//    suspend fun findAllByFirstname(id: String): List<Item>
}

private val logger = KotlinLogging.logger {}

@Controller
class ItemController(private val itemRepository: ItemRepository) {

    // --- Query ---
    @QueryMapping
    fun listItems(): Flow<Item> {
        logger.atDebug().log("listing items")
        return itemRepository.findAll()
    }

    // --- Association ---

//    @BatchMapping
//    fun store(
//        // Must use `java.util.List` because Spring-GraphQL has a bug: #454
//        books: java.util.List<Book>
//    ): Map<Book, BookStore> =
//        bookRepository.graphql.load(Book::store, books)

    // --- Mutation ---
    @MutationMapping
    @Transactional
    suspend fun addItem(@Argument input: ItemInput): Item {
        logger.atDebug().addKeyValue("input", input).log("adding item")
        return itemRepository.save(input.toEntity())
    }

    @MutationMapping
    @Transactional
    suspend fun deleteItem(@Argument id: Long): Item? {
        logger.atDebug().addKeyValue("id", id).log("deleting item")
        return itemRepository.findById(id)
            ?.also { itemRepository.deleteById(id) }
    }
}
