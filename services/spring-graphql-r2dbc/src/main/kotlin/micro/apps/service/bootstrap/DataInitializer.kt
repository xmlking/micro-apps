package micro.apps.service.bootstrap

import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import micro.apps.service.domain.item.Item
import micro.apps.service.domain.item.ItemRepository
import mu.KotlinLogging
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger {}

/**
 * `application.runner.enabled` set to `false` during tests
 */
@Component
@ConditionalOnProperty(
    value = ["application.runner.enabled"],
    havingValue = "true",
    matchIfMissing = true
)
class DataInitializer(
    private val itemRepository: ItemRepository
) : ApplicationRunner {

    override fun run(args: ApplicationArguments) {
        val data = listOf(
            Item(name = "sumo", description = "demo"),
            Item(name = "auto", description = "rama")
        )

        runBlocking {
            if (itemRepository.count() == 0L) {
                itemRepository.saveAll(data)
                    .map {
                        logger.atDebug().log("saved: $it")
                    }.collect()
            }
        }
    }
}
