package micro.apps.service

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.greaterThanOrEqualTo
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@QuarkusTest
@Tag("integration")
class GreetingResourceTest {

    @Test
    fun testHelloEndpoint() {

        val query = """
            {
                hello
            }
        """.trimIndent()

        given()
            .contentType("application/json")
            .accept("application/json")
            .body(GraphQlQuery(query = query))
            .`when`().post("/graphql")
            .then()
            .statusCode(200)
            .body("data.hello", equalTo("hello"))
            .and()
            .body(containsString("hello"))
    }

    @Test
    fun testGetProductsEndpoint() {

        val query = """
            {
                products
            }
        """.trimIndent()

        given()
            .contentType("application/json")
            .accept("application/json")
            .body(GraphQlQuery(query = query))
            .`when`().post("/graphql")
            .then()
            .statusCode(200)
            .body(`is`("{\"data\":{\"products\":[\"test\",\"test1\",\"test2\"]}}"))
            .body("data.products[0]", equalTo("test"))
            .body("data.products.size", greaterThanOrEqualTo(3))
            .and()
            .body(containsString("test"))
    }
}

@Suppress("unused")
class GraphQlQuery(val param: String = "query", val query: String)
