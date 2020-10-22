package micro.apps.greeting

import io.kotest.core.spec.style.FunSpec
import io.kotest.core.test.TestCaseConfig
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import javax.ws.rs.core.MediaType
import kotlin.time.ExperimentalTime
import kotlin.time.minutes
import org.hamcrest.CoreMatchers
import org.hamcrest.Matchers

// FIXME https://github.com/kotest/kotest/issues/1401
@ExperimentalTime
@QuarkusTest
class FruitResourceTest : FunSpec({

    // defaultTestConfig
    TestCaseConfig(timeout = 3.minutes)

    beforeTest {
        println("Starting test ${it.displayName}!")
    }
    afterTest {
        println("Finished test ${it.a.displayName}!")
    }

    test("get fruits call should work").config(enabled = false) {
        RestAssured
            .given()
            .`when`()
            .get("/api/fruits")
            .then()
            .statusCode(200)
            .body("$.size()", CoreMatchers.`is`(2),
                "name", Matchers.containsInAnyOrder("Apple", "Pineapple"),
                "description", Matchers.containsInAnyOrder("Winter fruit", "Tropical fruit"))
    }

    test("add fruits call should work").config(enabled = false) {
        RestAssured
            .given()
            .body("{\"name\": \"Pear\", \"description\": \"Winter fruit\"}")
            .contentType(MediaType.APPLICATION_JSON)
            .`when`()
            .post("/api/fruits")
            .then()
            .statusCode(200)
            .body("$.size()", CoreMatchers.`is`(3),
                "name", Matchers.containsInAnyOrder("Apple", "Pineapple", "Pear"),
                "description", Matchers.containsInAnyOrder("Winter fruit", "Tropical fruit", "Winter fruit"))

        RestAssured
            .given()
            .body("{\"name\": \"Pear\", \"description\": \"Winter fruit\"}")
            .contentType(MediaType.APPLICATION_JSON)
            .`when`()
            .delete("/api/fruits")
            .then()
            .statusCode(200)
            .body("$.size()", CoreMatchers.`is`(2),
                "name", Matchers.containsInAnyOrder("Apple", "Pineapple"),
                "description", Matchers.containsInAnyOrder("Winter fruit", "Tropical fruit"))
    }
})
