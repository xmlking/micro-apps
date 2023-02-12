package micro.apps.service

import io.kotest.matchers.nulls.shouldNotBeNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest
@AutoConfigureWebTestClient
class DemoApplicationTests(@Autowired private val webTestClient: WebTestClient) {

    @Test
    fun contextLoads() {
        // when
        val schema =
            webTestClient
                .get()
                .uri("/gql")
                .exchange()
                .returnResult(String::class.java)
                .responseBody
                .reduce { a, b -> a + "\n" + b }
                .block()

        // then
        schema.shouldNotBeNull()
    }
}
