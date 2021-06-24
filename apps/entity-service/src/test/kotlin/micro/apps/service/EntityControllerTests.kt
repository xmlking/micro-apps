package micro.apps.service

import com.ninjasquad.springmockk.MockkBean
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.FunSpec
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.serialization.ExperimentalSerializationApi
import micro.apps.model.Person
import micro.apps.model.fixtures.mockPerson
import micro.apps.test.Mock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody

@OptIn(ExperimentalSerializationApi::class)
@WebFluxTest
@ContextConfiguration(classes = [EntityController::class])
class EntityControllerTests(@Autowired private val client: WebTestClient) : FunSpec() {

    @MockkBean
    lateinit var repository: EntityRepository

    init {
        beforeTest {
            println("before test...")
        }

        test("test EntityController.intro") {
            val response = client.get()
                .uri("/intro")
                .exchange()

            withClue("Fails with this clue") {
                response.expectStatus().isOk
                    .expectBody<String>().isEqualTo("Hello")
            }
        }

        test("test EntityController.entity").config(enabled = true, tags = setOf(Mock)) {
            // given
            val id = "www"
            coEvery { repository.get(id) } returns mockPerson(1)

            // when
            val response = client.get()
                .uri("/entity/$id")
                .exchange()

            // then
            withClue("Fails with this clue") {
                response.expectStatus().isOk
                    .expectBody<Person>().isEqualTo(mockPerson(1))
            }

            coVerify(exactly = 1) { repository.get(id) }
        }
    }
}
