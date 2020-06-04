package micro.apps.greeting

import io.kotest.core.spec.style.FunSpec
import io.kotest.core.test.TestCaseConfig
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import kotlin.time.ExperimentalTime
import kotlin.time.minutes
import org.hamcrest.CoreMatchers.equalTo

// FIXME https://github.com/kotest/kotest/issues/1401
@ExperimentalTime
@QuarkusTest
class GreetingResourceTest : FunSpec({

    // defaultTestConfig
    TestCaseConfig(timeout = 3.minutes)

    beforeTest {
        println("Starting test ${it.name}!")
    }
    afterTest {
        println("Finished test ${it.a.name}!")
    }

    test("testGreetingEndpoint").config(enabled = false) {
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