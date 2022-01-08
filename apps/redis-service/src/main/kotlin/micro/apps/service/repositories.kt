package micro.apps.service

import com.redis.om.spring.annotations.Aggregation
import com.redis.om.spring.annotations.Query
import com.redis.om.spring.repository.RedisDocumentRepository
import io.redisearch.AggregationResult
import micro.apps.model.Gender
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*


@Repository
interface UserRepository : CrudRepository<User, String> {
    fun findOneByLastName(lastName: String): Optional<User>
    fun findByFirstNameAndLastName(firstName: String, lastName: String): List<User>
    fun findByLastNameStartsWithIgnoreCase(lastName: String): List<User>
    fun existsByEmail(email: String): Boolean
}

@Repository
interface RoleRepository : CrudRepository<Role, String> {
    fun findOneByRoleName(roleName: String): Optional<Role>
}

@Repository
interface PersonRepository : RedisDocumentRepository<Person, String> {
    // Performing a text search on all text fields:
    // FT.SEARCH permits "sumo"
    // Result: Documents inside which the word 'sumo' occurs
    fun search(text: String): List<Person>
    fun findByName_FirstStartsWithIgnoreCase(name: String): List<Person>
    fun findByGender(gender: Gender): List<Person>
    fun existsByEmail(email: String): Boolean

    /**
     * > FT.SEARCH idx '@title:hello @tag:{news}'
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


