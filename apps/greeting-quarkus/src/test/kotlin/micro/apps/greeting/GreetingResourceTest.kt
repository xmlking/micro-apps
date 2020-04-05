package micro.apps.greeting

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
// import org.hamcrest.CoreMatchers.`is`
// import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.Test

@QuarkusTest
open class GreetingResourceTest {

    @Test
    fun testGreetingEndpoint() {
        val response = given().`when`().get("/api/v1/greeting").asString()
        println("Response: $response")

        given()
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .log().all()
        .`when`()
            .get("/api/v1/greeting")
        .then()
            .statusCode(200)
            // .body(containsString("hello"))
            .contentType(ContentType.JSON)
            // .body("message") { `is`("hello") }
            .body("message", equalTo("hello"))
    }
}
