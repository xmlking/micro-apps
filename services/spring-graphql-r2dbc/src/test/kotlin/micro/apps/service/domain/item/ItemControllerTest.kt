package micro.apps.service.domain.item

import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.FunSpec
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.flow.flowOf
import mu.KotlinLogging
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureHttpGraphQlTester
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.graphql.test.tester.HttpGraphQlTester

private val logger = KotlinLogging.logger {}

@SpringBootTest()
@AutoConfigureHttpGraphQlTester
internal class ItemControllerTest(
    @MockkBean
    private val itemRepository: ItemRepository,
    private val graphQlTester: HttpGraphQlTester
) : FunSpec({

    lateinit var input: ItemInput

    beforeTest {
        println("before each test...")
        input = ItemInput("sumo", "demo")
    }
    test("add item") {
        // given
        val item = Item(1, "sumo", "demo")
        coEvery { itemRepository.save(any()) } returns item

        // when
        val response = graphQlTester
            .documentName("MUTATION.AddItem")
            .variable("input", input)
            .execute()

        // then
        response
            .path("data.addItem")
            .entity(Item::class.java)
            .isEqualTo(item)

        coVerify(exactly = 1) { itemRepository.save(any()) }
    }

    test("list").config(enabled = false) {
        // given
        coEvery { itemRepository.findAll() } returns flowOf(
            Item(1, "sumo1", "demo1"),
            Item(2, "sumo2", "demo2")
        )

        // when
        val response = graphQlTester
            .documentName("QUERY.ListItems")
            .execute()

        // then
        response
            .path("data.listItems[*].name")
            .entityList(String::class.java)
            .hasSize(2).contains("sumo1", "sumo2")

        coVerify(exactly = 1) { itemRepository.findAll() }
    }
})
