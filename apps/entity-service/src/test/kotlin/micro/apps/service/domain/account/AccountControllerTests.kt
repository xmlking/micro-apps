package micro.apps.service.domain.account

import com.ninjasquad.springmockk.MockkBean
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.FunSpec
import io.kotest.core.test.TestCaseConfig
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.ExperimentalSerializationApi
import micro.apps.test.Mock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.MediaType
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
        // defaultTestConfig
        TestCaseConfig(tags = setOf(Mock))

        beforeTest {
            println("before test...")
        }

        test("test AccountController.getPeople") {
            // given
            val mockPerson = mockPersonDto(1)
            coEvery { accountService.getAllPeople() } returns mockPersonList()

            // when
            val response = client.get().uri("/account")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()

            // then
            withClue("Fails with this clue") {
                response.expectStatus().isOk
                    .expectBody<Flow<PersonEntity>>().isEqualTo(mockPersonList())
            }

            coVerify(exactly = 1) { accountService.getAllPeople() }
        }

        test("test valid AccountController.createPerson") {
            // given
            val mockPerson = mockPersonDto(1)
            println(mockPerson)
            coEvery { accountService.createPerson(mockPerson) } returns mockPersonEntity(1)

            // when
            val response = client.post().uri("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
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
