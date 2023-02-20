package micro.apps.service.domain.book

import io.kotest.core.spec.style.FunSpec
import micro.apps.test.E2E
import mu.KotlinLogging
import org.flywaydb.core.Flyway
import org.springframework.boot.test.context.SpringBootTest

/**
 * TODO https://maciejwalkowiak.com/blog/spring-boot-flyway-clear-database-integration-tests/
 */
private val logger = KotlinLogging.logger {}

@SpringBootTest()
internal class BookIntegrationTest(
    private val bookService: BookService,
    private val flyway: Flyway
) : FunSpec({

    lateinit var input: CreateBookInput

    beforeTest {
        logger.atDebug().log("before each test...")
        flyway.clean()
        flyway.migrate()
    }

    test("list").config(enabled = true, tags = setOf(E2E)) {
    }
})
