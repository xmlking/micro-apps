package micro.apps.service.domain.book

import com.github.f4b6a3.uuid.UuidCreator
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import mu.KotlinLogging
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureHttpGraphQlTester
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.graphql.test.tester.HttpGraphQlTester

private val logger = KotlinLogging.logger {}

@SpringBootTest()
@AutoConfigureHttpGraphQlTester
internal class BookControllerTest(
    @MockkBean
    private val bookService: BookService,
    private val graphQlTester: HttpGraphQlTester
) : FunSpec({

    lateinit var input: CreateBookInput

    beforeTest {
        logger.atDebug().log("before each test...")
        input = CreateBookInput(title = "sumo", pages = 100, category = Category.HORROR, author = "demo")
    }

    test("create book") {
        // given
        val book = Book(
            id = UuidCreator.getTimeOrderedEpoch(),
            title = input.title,
            pages = input.pages,
            category = input.category
        )

        coEvery { bookService.createBook(any()) } returns book

        // when
        val response = graphQlTester
            .documentName("MUTATION.createBook")
            .variable("input", input)
            .execute()

        // then
        response
            .path("data.createBook")
            .hasValue()
            .entity(Book::class.java).satisfies {
                it.title shouldBe input.title
                it.category shouldBe input.category
            }

        coVerify(exactly = 1) { bookService.createBook(any()) }
    }

    test("list").config(enabled = true) {
    }
})
