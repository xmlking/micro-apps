package micro.apps.service.domain.book

import kotlinx.coroutines.channels.BufferOverflow.DROP_OLDEST
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.reactive.asPublisher
import mu.KotlinLogging
import org.reactivestreams.Publisher
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SubscriptionMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller
import java.security.Principal
import java.util.UUID

private val logger = KotlinLogging.logger {}

@Controller
class BookController(private val bookService: BookService) {
    private val _events = MutableSharedFlow<Book>( // private mutable shared flow
        replay = 1,
        extraBufferCapacity = 10,
        onBufferOverflow = DROP_OLDEST
    )
    val events = _events.asSharedFlow() // publicly exposed as read-only shared flow

    // private val sink1 = Sinks.many().replay().limit<Book>(10, Duration.ofSeconds(2))
    // private val sink = Sinks.many().multicast().onBackpressureBuffer<Book>(20)

    // --- Queries ---
    @QueryMapping
    suspend fun bookById(@Argument id: UUID): Book? {
        val book = bookService.bookById(id)
        logger.atDebug().log("get bookById: {}", book)
        return book
    }

    // --- SchemaMapping ---
    @SchemaMapping
    suspend fun author(book: Book): Author? {
        return book.id?.let { bookService.authorByBookId(it) }
    }

    // --- Associations ---

    // --- Mutations ---
    @MutationMapping
    suspend fun createBook(@Argument input: CreateBookInput): Book {
        logger.atDebug().addKeyValue("input", input).log("create book")
        return bookService.createBook(input).also { _events.emit(it) }
        // return bookService.createBook(input).also { sink.emitNext(book, Sinks.EmitFailureHandler.FAIL_FAST)  } // tryEmitNext
    }

    // --- Subscriptions ---
    @SubscriptionMapping
    fun bookStream(@Argument category: Category, principal: Principal): Publisher<Book> {
        logger.atDebug().log("bookStream subscription called,category: {} principal: {}", category, principal.name)
        return events.filter { it.category == category }.asPublisher()
        // return events.asPublisher()
    }

    /*
    @SubscriptionMapping
    fun bookAdded(@Argument category: Category, @AuthenticationPrincipal jwt: Jwt): Publisher<Book> {
        logger.atDebug().log("called messages subscription, jwt: {}", jwt)
        return sink.asFlux()
            .cache()
            .doOnComplete { logger.info("Stream completed") }
            .doOnError { logger.error("Something when wrong with the stream", it) }
    }
    */
}
