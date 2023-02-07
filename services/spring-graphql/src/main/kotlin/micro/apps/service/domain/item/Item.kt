package micro.apps.service.domain.item

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import mu.KotlinLogging
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.data.repository.CrudRepository
import org.springframework.graphql.data.GraphQlRepository
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.stereotype.Controller

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
interface ItemRepository : CrudRepository<Item, Long>, QuerydslPredicateExecutor<Item>

// @GraphQlRepository
// interface ItemRepository : PagingAndSortingRepository<Item, Long>, QuerydslPredicateExecutor<Item> {
//    @Query("select m from Message m where m.to.id = ?#{ principal?.id }")
//    fun findInbox(pageable: Pageable?): Page<Message?>?
// }

private val logger = KotlinLogging.logger {}

@Controller
class ItemController(private val itemRepository: ItemRepository) {

    @MutationMapping
    fun addItem(@Argument input: ItemInput): Item {
        logger.atDebug().addKeyValue("input", input).log("adding item")
        return itemRepository.save(input.toEntity())
    }

    @MutationMapping
    fun deleteItem(@Argument id: Long): Item? {
        logger.atDebug().addKeyValue("id", id).log("deleting item")
        return itemRepository.findById(id)
            .orElse(null)
            ?.also { itemRepository.deleteById(id) }
    }
}
