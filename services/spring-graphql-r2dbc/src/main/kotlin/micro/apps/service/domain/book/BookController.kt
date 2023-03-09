package micro.apps.service.domain.book

@Controller
class BookController(private val bookService: BookService) {
    private val _events = MutableSharedFlow<Book>(1, 10) // private mutable shared flow
    val events = _events.asSharedFlow() // publicly exposed as read-only shared flow

    // private val sink1 = Sinks.many().replay().limit<Book>(10, Duration.ofSeconds(2))
    // private val sink = Sinks.many().multicast().onBackpressureBuffer<Book>(20)

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
        return bookService.createBook(input).also { _events.emit(it) }
        // return bookService.createBook(input).also { sink.emitNext(book, Sinks.EmitFailureHandler.FAIL_FAST)  } // tryEmitNext
    }

    // --- Subscriptions ---
    @SubscriptionMapping
    fun bookStream(principal: Principal): Publisher<Book> {
        logger.atDebug().log("called bookStream subscription, principal: {}", principal.name)
        return events.asPublisher()
    }

    /*
    @SubscriptionMapping
    fun bookStream(@Argument count: Int, @AuthenticationPrincipal jwt: Jwt): Publisher<Book> {
        logger.atDebug().log("called messages subscription, jwt: {}", jwt)
        return sink.asFlux()
            .cache()
            .doOnComplete { logger.info("Stream completed") }
            .doOnError { logger.error("Something when wrong with the stream", it) }
    }
    */
}
