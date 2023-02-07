package micro.apps.service.domain.account

import kotlinx.serialization.ExperimentalSerializationApi
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

/*
 * Note: Redis Repositories DO NOT work with transactions
 */
// interface PersonRepository : ReactiveCrudRepository<PersonEntity, String> {
// interface PersonRepository : CoroutineSortingRepository<PersonEntity, String> {
@OptIn(ExperimentalSerializationApi::class)
@Repository
interface Person1Repository : PagingAndSortingRepository<PersonEntity, String>

@OptIn(ExperimentalSerializationApi::class)
@Repository
interface AddressRepository : PagingAndSortingRepository<AddressEntity, String>
