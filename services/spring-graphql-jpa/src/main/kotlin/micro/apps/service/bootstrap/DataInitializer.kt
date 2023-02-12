package micro.apps.service.bootstrap

import kotlinx.coroutines.runBlocking
import micro.apps.service.domain.item.Item
import micro.apps.service.domain.item.ItemRepository
import mu.KotlinLogging
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger {}

/**
 * `command.line.runner.enabled` set to `false` during tests
 */
@Component
@ConditionalOnProperty(
    value = ["command.line.runner.enabled"],
    havingValue = "true",
    matchIfMissing = true
)
class DataInitializer(
    private val itemRepository: ItemRepository
) : CommandLineRunner {

    override fun run(vararg args: String?) {
        val data = listOf(
            Item(name = "sumo", description = "demo"),
            Item(name = "auto", description = "rama")
        )

        runBlocking {
            if (itemRepository.count() == 0L) {
                itemRepository.saveAll(data)
                    .map {
                        logger.atDebug().log("saved: $it")
                    }
            }
        }
    }
}
