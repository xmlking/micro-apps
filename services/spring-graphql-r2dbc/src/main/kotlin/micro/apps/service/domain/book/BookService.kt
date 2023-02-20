package micro.apps.service.domain.book

import org.springframework.stereotype.Service
import com.github.f4b6a3.uuid.UuidCreator
import mu.KotlinLogging
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

private val logger = KotlinLogging.logger {}
@Service
class BookService(private val bookRepository: BookRepository, private val authorRepository: AuthorRepository) {

    // FIXME after: https://github.com/spring-projects/spring-framework/issues/29829
    // @Transactional
    suspend fun createBook(input: CreateBookInput): Book {
        val book = bookRepository.save(
            Book(id = UuidCreator.getTimeOrderedEpoch(), title = input.title, pages = input.pages, category = input.category )
        )
        logger.atDebug().addKeyValue("book",book).log("saved book")
        authorRepository.save(
            Author(id = UuidCreator.getTimeOrderedEpoch(), name = input.author, age = 0, bookId = book.id )
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
    suspend fun getBook(id: UUID): Book? {
        return bookRepository.findById(id)
    }
}
