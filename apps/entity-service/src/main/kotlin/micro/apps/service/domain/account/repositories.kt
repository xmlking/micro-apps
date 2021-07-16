package micro.apps.service.domain.account

import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.ExperimentalSerializationApi
import org.springframework.data.domain.Sort
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.kotlin.CoroutineSortingRepository
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository

@OptIn(ExperimentalSerializationApi::class)
@Repository
//interface PersonRepository : ReactiveCrudRepository<PersonEntity, String> {
//interface PersonRepository : CoroutineSortingRepository<PersonEntity, String> {
interface PersonRepository: PagingAndSortingRepository<PersonEntity, String> {
}

@OptIn(ExperimentalSerializationApi::class)
@Repository
interface AddressRepository: PagingAndSortingRepository<AddressEntity, String> {
}
