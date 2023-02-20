package micro.apps.service.domain.book

import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.kotlin.CoroutineSortingRepository
import org.springframework.graphql.data.GraphQlRepository
import java.util.UUID

@GraphQlRepository
interface BookRepository : CoroutineCrudRepository<Book, UUID>, CoroutineSortingRepository<Book, UUID> {
    @Query("SELECT * FROM books WHERE name = @name")
    fun findAllByName(name: String): Flow<Book>
}
