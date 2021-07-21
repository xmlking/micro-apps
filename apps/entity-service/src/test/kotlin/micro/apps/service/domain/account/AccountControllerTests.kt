package micro.apps.service.domain.account

import com.ninjasquad.springmockk.MockkBean
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.FunSpec
import io.kotest.core.test.TestCaseConfig
import io.mockk.Called
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.serialization.ExperimentalSerializationApi
import micro.apps.test.Mock
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody

@OptIn(ExperimentalSerializationApi::class)
@WebFluxTest
@ContextConfiguration(classes = [AccountController::class])
class AccountControllerTests(private val client: WebTestClient) : FunSpec() {

    @MockkBean
    lateinit var accountService: AccountService

    init {
        // defaultTestConfig
        TestCaseConfig(tags = setOf(Mock))

        beforeTest {
            println("before test...")
        }

        test("GET all people returns list of people as Flow") {
            // given
            coEvery { accountService.getAllPeople() } returns mockPersonList().asFlow()

            // when
            val response = client.get().uri("/account")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()

            // then
            withClue("Fails with this clue") {
                response.expectStatus().isOk
                    .expectBody<List<PersonEntity>>().isEqualTo(mockPersonList())
            }

            coVerify(exactly = 1) { accountService.getAllPeople() }
        }

        test("GET all people returns empty array if no person exist") {
            // given
            coEvery { accountService.getAllPeople() } returns emptyFlow()

            // when
            val response = client.get().uri("/account")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()

            // then
            response.expectStatus().isOk
                .expectBody<Array<PersonEntity>>()
                .isEqualTo(arrayOf())

            coVerify(exactly = 1) { accountService.getAllPeople() }
        }

        test("POST valid PersonDto should create and return PersonEntity") {
            // given
            val mockPersonDto = mockPersonDto(1)
            coEvery { accountService.createPerson(mockPersonDto) } returns mockPersonEntity(1)

            // when
            val response = client.post().uri("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(mockPersonDto)
                .exchange()

            // then
            withClue("Fails with this clue") {
                response.expectStatus().isAccepted
                    // .expectBody()
                    // .jsonPath("$.name").isNotEmpty
                    // .jsonPath("$.name.first").isEqualTo("first1")
                    // .jsonPath("$.addresses.[0].street").isEqualTo("first line 1")
                    .expectBody<PersonEntity>()
                    .isEqualTo(mockPersonEntity(1))
            }

            coVerify(exactly = 1) { accountService.createPerson(mockPersonDto) }
        }

        test("POST invalid PersonDto should throw validation error") {
            // given
            val mockPersonDtoInvalid = mockPersonDto(1).copy(age = 11)
            coEvery { accountService.createPerson(mockPersonDtoInvalid) } returns mockPersonEntity(1)

            // when
            val response = client.post().uri("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(mockPersonDtoInvalid)
                .exchange()

            // then
            withClue("Fails with this clue") {
                response.expectStatus().isBadRequest
                    .expectBody()
                    .jsonPath("$.status").isEqualTo(400)
                    .jsonPath("$.error").isEqualTo("Bad Request")
            }

            coVerify { accountService wasNot Called }
            // coVerify(inverse = true) { accountService.createPerson(mockPersonDtoInvalid) }
        }
    }
}
