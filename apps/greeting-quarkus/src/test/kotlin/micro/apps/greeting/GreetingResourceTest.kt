package micro.apps.greeting

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given

import org.junit.jupiter.api.Test
import org.hamcrest.CoreMatchers.`is`

@QuarkusTest
open class GreetingResourceTest {

    @Test
    fun testGreetingEndpoint() {
        given()
          .`when`().get("/v1/api/greeting")
          .then()
             .statusCode(200)
            .body(`is`("hello"));
    }

}
