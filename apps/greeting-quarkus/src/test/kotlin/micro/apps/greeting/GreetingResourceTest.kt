package micro.apps.greeting

import io.kotest.core.spec.style.FunSpec
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers.equalTo

@QuarkusTest
class GreetingResourceTest : FunSpec({

    beforeTest {
        println("Starting test ${it.name}!")
    }
    afterTest {
        println("Finished test ${it.a.name}!")
    }

    test("testGreetingEndpoint") {
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
})
