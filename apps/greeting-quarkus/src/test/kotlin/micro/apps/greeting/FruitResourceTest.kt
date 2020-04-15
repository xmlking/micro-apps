package micro.apps.greeting

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import javax.ws.rs.core.MediaType
import org.hamcrest.CoreMatchers
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test

@QuarkusTest
class FruitResourceTest {
    @Test
    fun testList() {
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

    @Test
    fun testAdd() {
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
}
