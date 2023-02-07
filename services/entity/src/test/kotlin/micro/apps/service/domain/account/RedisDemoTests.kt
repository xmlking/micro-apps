//package micro.apps.service.domain.account
//
//import io.kotest.core.spec.style.FunSpec
//import io.kotest.core.test.config.TestCaseConfig
//import kotlinx.serialization.ExperimentalSerializationApi
//import micro.apps.test.E2E
//import micro.apps.test.Slow
//import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest
//
//@OptIn(ExperimentalSerializationApi::class)
//@DataRedisTest
//class RedisDemoTests(
//    private val moduleOps: RedisModulesOperations<String, String>
//) : FunSpec({
//    // defaultTestConfig
//    TestCaseConfig(tags = setOf(E2E, Slow))
//
//    val aPersonId = "1073516001"
//    lateinit var aPerson: PersonEntity
//    var graph: GraphOperations<String> = moduleOps.opsForGraph()
//    RedisDocumentRepository<Company, String>
//
//    beforeSpec {
//        println("***DID YOU RUN `FLUSHDB` REDIS COMMAND TO CLEAN THE DATABASE?")
//    }
//
//    afterSpec {
//    }
//
//    test("test 1") {
//        println("test 1")
//        graph("social", "CREATE(:person{name:'sumo', age:135})")
//    }
//})
