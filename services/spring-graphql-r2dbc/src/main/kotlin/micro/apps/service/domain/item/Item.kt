package micro.apps.service.domain.item

import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import kotlinx.coroutines.flow.toList
import mu.KotlinLogging
import org.springframework.data.annotation.Id
import org.springframework.data.domain.Sort
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
//    fun findByName(name: String, page: Pageable): Flow<Item>
//    fun findAll(pageable: Pageable): Flow<Item>
}

private val logger = KotlinLogging.logger {}

@Controller
class ItemController(private val itemRepository: ItemRepository) {

    // --- Query ---
    @Transactional(readOnly = true)
    @QueryMapping
    // FIXME: https://github.com/spring-projects/spring-graphql/issues/393
    // fun listItems(): Flow<Item> {
    suspend fun listItems(): List<Item> {
        logger.atDebug().log("listing items")
        return itemRepository.findAll().toList()
    }

    @Transactional(readOnly = true)
    @QueryMapping
    // FIXME: https://github.com/spring-projects/spring-graphql/issues/393
    // fun findAll(@Argument offset: Int = 0, @Argument limit: Int = 100, @Argument orderBy: String = "name"): Flow<Item> {
    suspend fun findAll(@Argument offset: Int = 0, @Argument limit: Int = 100, @Argument orderBy: String = "name"): List<Item> {
        // val pageRequest = PageRequest.of(offset, limit, Sort.by(Sort.Direction.ASC, orderBy))
        // val content = itemRepository.findAll(pageRequest)
        // return PageImpl(content.toList(), pageRequest, itemRepository.count())
        return itemRepository.findAll(Sort.by(Sort.Direction.ASC, orderBy)).toList()
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
