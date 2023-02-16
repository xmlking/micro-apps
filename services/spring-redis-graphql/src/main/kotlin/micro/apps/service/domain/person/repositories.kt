package micro.apps.service.domain.person

import com.redis.om.spring.repository.RedisDocumentRepository
import micro.apps.model.Gender
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface PersonRepository : RedisDocumentRepository<Person, String> {
    // Performing a text search on all text fields:
    // FT.SEARCH permits "sumo"
    // Result: Documents inside which the word 'sumo' occurs
    fun search(text: String): List<Person>
    fun findByName_FirstStartsWithIgnoreCase(name: String): List<Person>
    fun findByGender(gender: Gender): List<Person>
    fun existsByEmail(email: String): Boolean

    // Performing a tag search on city
    fun findByAddress_City(city: String): Iterable<Person?>

    // Performing a full-search on street
    fun findByAddress_CityAndAddress_State(city: String, state: String): Iterable<Person?>

    /**
     * > FT.SEARCH idx '@title:hello @tag:{news}
     * 1) (integer) 1 2) "doc1" 3) 1) "$"
     * 2) "{\"title\":\"hello world\",\"tag\":[\"news\",\"article\"]}"
     */
//    @Query("@title:\$title @tag:{\$tags}")
//    fun findByTitleAndTags(@Param("title") title: String, @Param("tags") tags: Set<String>): List<Person>

    /**
     * > FT.AGGREGATE idx * LOAD 3 $.tag[1] AS tag2
     * 1) (integer) 1
     * 2) 1) "tag2"
     * 2) "article"
     */
//    @Aggregation(load = ["$.tag[1]", "AS", "tag2"])
//    fun getSecondTagWithAggregation(): AggregationResult
}
