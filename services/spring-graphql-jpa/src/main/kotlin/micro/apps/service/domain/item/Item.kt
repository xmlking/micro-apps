package micro.apps.service.domain.item

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import kotlinx.coroutines.flow.toList
import mu.KotlinLogging
import org.springframework.data.domain.Sort
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.graphql.data.GraphQlRepository
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Transactional

@Entity
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
interface ItemRepository : CrudRepository<Item, Long>, PagingAndSortingRepository<Item, Long>, QuerydslPredicateExecutor<Item>

// @GraphQlRepository
// interface ItemRepository : PagingAndSortingRepository<Item, Long>, QuerydslPredicateExecutor<Item> {
//    @Query("select m from Message m where m.to.id = ?#{ principal?.id }")
//    fun findInbox(pageable: Pageable?): Page<Message?>?
// }

private val logger = KotlinLogging.logger {}

@Controller
class ItemController(private val itemRepository: ItemRepository) {

    // --- Query ---
    @QueryMapping
    fun listItems(): Iterable<Item> {
        logger.atDebug().log("listing items")
        return itemRepository.findAll()
    }

    @QueryMapping
    fun findAll(@Argument offset: Int = 0, @Argument limit: Int = 100, @Argument orderBy: String = "name"): List<Item> {
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
    fun addItem(@Argument input: ItemInput): Item {
        logger.atDebug().addKeyValue("input", input).log("adding item")
        return itemRepository.save(input.toEntity())
    }

    @MutationMapping
    @Transactional
    fun deleteItem(@Argument id: Long): Item? {
        logger.atDebug().addKeyValue("id", id).log("deleting item")
        return itemRepository.findById(id)
            .orElse(null)
            ?.also { itemRepository.deleteById(id) }
    }
}
