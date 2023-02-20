package micro.apps.service.domain.book

import com.github.f4b6a3.uuid.UuidCreator
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.reactive.asPublisher
import mu.KotlinLogging
import org.reactivestreams.Publisher
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SubscriptionMapping
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Controller
import java.util.UUID

private val logger = KotlinLogging.logger {}

@Controller
class BookController(private val bookService: BookService) {

    // --- Queries ---
    @QueryMapping
    suspend fun getBook(@Argument id: UUID): Book? {
        val book = bookService.getBook(id)
        logger.atDebug().log("get book: {}", book)
        return book
    }

    // --- Associations ---

    // --- Mutations ---
    @MutationMapping
    suspend fun createBook(@Argument input: CreateBookInput): Book {
        logger.atDebug().addKeyValue("input", input).log("create book")
        return bookService.createBook(input)
    }

    // --- Subscriptions ---
    @SubscriptionMapping
    fun bookStream(@Argument count: Int, @AuthenticationPrincipal jwt: Jwt): Publisher<Book> {
        logger.atDebug().log("called messages subscription, jwt: {}", jwt)
        return flow {
            emit(Book(id = UuidCreator.getTimeOrderedEpoch(), title = "sumo", pages = 99, category = Category.FANTASY))
            for (n in count downTo 1) {
                delay(5000)
                emit(Book(id = UuidCreator.getTimeOrderedEpoch(), title = "sumo", pages = 99, category = Category.FANTASY))
            }
        }.cancellable().buffer().asPublisher()
    }
}
