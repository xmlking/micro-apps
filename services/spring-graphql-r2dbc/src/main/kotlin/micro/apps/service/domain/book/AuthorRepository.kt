package micro.apps.service.domain.book

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.kotlin.CoroutineSortingRepository
import org.springframework.graphql.data.GraphQlRepository
import java.util.UUID

@GraphQlRepository
interface AuthorRepository : CoroutineCrudRepository<Author, UUID>, CoroutineSortingRepository<Author, UUID> {
    suspend fun findAuthorByBookId(id: UUID): Author
}
