package micro.apps.service.domain.account

import com.ninjasquad.springmockk.MockkBean
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.FunSpec
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.serialization.ExperimentalSerializationApi
import micro.apps.test.Mock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody

@OptIn(ExperimentalSerializationApi::class)
@WebFluxTest
@ContextConfiguration(classes = [AccountController::class])
class AccountControllerTests(@Autowired private val client: WebTestClient) : FunSpec() {

    @MockkBean
    lateinit var accountService: AccountService

    init {
        beforeTest {
            println("before test...")
        }

        test("test AccountController.getPeople") {
            val response = client.get()
                .uri("/account")
                .exchange()

            withClue("Fails with this clue") {
                response.expectStatus().isOk
                    .expectBody<Flow<PersonEntity>>().isEqualTo(mockPersonList().asFlow())
            }
        }

        test("test valid AccountController.createPerson").config(enabled = true, tags = setOf(Mock)) {
            // given
            val mockPerson = mockPersonDto(1)
            coEvery { accountService.createPerson(mockPerson) } returns mockPersonEntity(1)

            // when
            val response = client.get()
                .uri("/account")
                .exchange()

            // then
            withClue("Fails with this clue") {
                response.expectStatus().isOk
                    .expectBody<PersonEntity>().isEqualTo(mockPersonEntity(1))
            }

            coVerify(exactly = 1) { accountService.createPerson(mockPerson) }
        }
    }
}
