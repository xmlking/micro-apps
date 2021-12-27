package micro.apps.service

import com.github.javafaker.Faker
import micro.apps.kstream.KSerdes.grouped
import micro.apps.kstream.KSerdes.producedWith
import mu.KotlinLogging
import org.apache.kafka.streams.KeyValue
import org.apache.kafka.streams.kstream.KStream
import org.apache.kafka.streams.state.QueryableStoreTypes.keyValueStore
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.stream.binder.kafka.streams.InteractiveQueryService
import org.springframework.context.annotation.Bean
import org.springframework.integration.support.MessageBuilder
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.Message
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.util.Locale
import java.util.UUID
import java.util.function.Consumer
import java.util.function.Function
import java.util.function.Supplier
import org.apache.kafka.streams.kstream.Materialized.`as` as materialized

private val logger = KotlinLogging.logger {}

@SpringBootApplication
class WordcountApplication {

    val locale = Locale.getDefault()

    @Bean
    fun produceChuckNorris(): Supplier<Message<String>> = Supplier {
        MessageBuilder.withPayload(Faker.instance().chuckNorris().fact())
            .setHeader(KafkaHeaders.MESSAGE_KEY, UUID.randomUUID().toString())
            .build()
    }

    @Bean
    fun consumeCounts(): Consumer<KStream<String, Long>> = Consumer {
        it.peek { k, v -> logger.info("COUNT: {} = {}", k, v) }
    }

    @Bean
    fun processWords(): Function<KStream<String?, String>, KStream<String, Long>> = Function {
        val countsStream = it
            .flatMapValues { value: String -> value.lowercase(locale).split("\\W+".toRegex()) }
            .filter { _, v -> v.isNotBlank() }
            .map { _: String?, value: String -> KeyValue(value, value) }
            .groupByKey(grouped<String, String>())
            // .windowedBy(TimeWindows.ofSizeWithNoGrace(ofMinutes(5)).advanceBy(ofMinutes(1))) // // hopping-windows
            .count(materialized("word-count-state-store"))
            .toStream()
            .peek { k, v -> logger.info("PROCESSES COUNTS: {}, {}", k, v) }

        // .peek { k, v -> logger.info("PROCESSES COUNTS: {}, {}, {}, {}", k.key(), v, k.window().start(), k.window().end()) }
        // .map { k, v -> KeyValue(k.key(), v) } // unwrap key: Windowed<String>

        countsStream.to("counts", producedWith<String, Long>())
        countsStream
    }
}

@RestController
class IQRestController(private val iqService: InteractiveQueryService) {
    @GetMapping("/iq/count/{word}")
    fun getCount(@PathVariable word: String): Long {
        val store = iqService.getQueryableStore("word-count-state-store", keyValueStore<String, Long>())
        return store[word]
    }
}

fun main(args: Array<String>) {
    runApplication<WordcountApplication>(*args)
}
