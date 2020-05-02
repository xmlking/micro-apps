package micro.apps.greeting

import io.kotest.core.spec.style.FunSpec
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import javax.ws.rs.core.MediaType
import org.hamcrest.CoreMatchers
import org.hamcrest.Matchers

@QuarkusTest
class FruitResourceTest : FunSpec({

    beforeTest {
        println("Starting test ${it.name}!")
    }
    afterTest {
        println("Finished test ${it.a.name}!")
    }

    test("get fruits call should work") {
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

    test("add fruits call should work") {
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
