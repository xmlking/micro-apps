package micro.apps.greeting

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.`is`
import org.junit.jupiter.api.Test

@QuarkusTest
open class GreetingResourceTest {

    @Test
    fun testGreetingEndpoint() {
        given()
            .`when`().get("/api/v1/greeting")
            .then()
            .statusCode(200)
            .body("message", `is`("hello"))
    }
}
