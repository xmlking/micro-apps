package micro.apps.service.domain.book

import mu.KotlinLogging
import org.springframework.stereotype.Service
import java.util.UUID
// import com.github.f4b6a3.uuid.UuidCreator

private val logger = KotlinLogging.logger {}

@Service
class BookService(private val bookRepository: BookRepository, private val authorRepository: AuthorRepository) {

    // FIXME after: https://github.com/spring-projects/spring-framework/issues/29829
    // @Transactional
    suspend fun createBook(input: CreateBookInput): Book {
        val book = bookRepository.save(
            Book(title = input.title, pages = input.pages, category = input.category)
        )
        logger.atDebug().addKeyValue("book", book).log("saved book")
        authorRepository.save(
            Author( name = input.author, age = 0, bookId = book.id)
        )
        return book
    }

    // FIXME after: https://github.com/spring-projects/spring-framework/issues/29829
    // @Transactional(readOnly = true)
    suspend fun count(): Long {
        return bookRepository.count()
    }

    // FIXME after: https://github.com/spring-projects/spring-framework/issues/29829
    // @Transactional(readOnly = true)
    suspend fun bookById(id: UUID): Book? {
        return bookRepository.findById(id)
    }

    suspend fun authorByBookId(bookId: UUID): Author? {
        return authorRepository.findAuthorByBookId(bookId)
    }
}
