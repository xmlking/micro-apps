package micro.apps.service.domain.item

import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.FunSpec
import io.mockk.coEvery
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureHttpGraphQlTester
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.graphql.test.tester.HttpGraphQlTester

@SpringBootTest
@AutoConfigureHttpGraphQlTester
internal class ItemControllerTest(
    @MockkBean
    private val itemRepository: ItemRepository,
    private val graphQlTester: HttpGraphQlTester
) : FunSpec({

    test("create") {
        // given
        val input = ItemInput("sumo", "demo")
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
    }

    test("list").config(enabled = false) {
        // given
        val item = Item(1, "sumo", "demo")
        coEvery { itemRepository.findAll() } returns listOf(item)

        // when
        val response = graphQlTester
            .documentName("QUERY.ListItems")
            .execute()

        // then
        response
            .path("data.items").entity(Item::class.java).isEqualTo(listOf(item))
    }
})
