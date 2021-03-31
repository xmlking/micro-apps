package micro.apps.person

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.greaterThanOrEqualTo
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@QuarkusTest
@Tag("integration")
class PersonResourceTest {

    @Test
    fun testGetPersonEndpoint() {

        val query = """
            {
              person(id: 1){
                names
                surname
                scores{
                  name
                  value
                }
              }
            }
        """.trimIndent()

        val response = """
            {
                "data": {
                    "person": {
                      "names": [
                        "Christine",
                        "Fabian"
                      ],
                      "surname": "O'Reilly",
                      "scores": [
                        {
                          "name": "Driving",
                          "value": 2
                        },
                        {
                          "name": "Fitness",
                          "value": 68
                        },
                        {
                          "name": "Activity",
                          "value": 53
                        },
                        {
                          "name": "Financial",
                          "value": 24
                        }
                      ]
                    }
                }
            }
        """.trimIndent().replace("\\s".toRegex(), "")
        given()
            .contentType("application/json")
            .accept("application/json")
            .body(GraphQlQuery(query = query))
            .`when`().post("/graphql")
            .then()
            .statusCode(200)
            .body(`is`(response))
            .body("data.person.surname", equalTo("O'Reilly"))
            .body("data.person.scores.size", greaterThanOrEqualTo(3))
    }
}
