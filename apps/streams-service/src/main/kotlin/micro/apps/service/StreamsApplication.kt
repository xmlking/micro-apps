package micro.apps.service

import mu.KotlinLogging
import org.apache.kafka.streams.kstream.KStream
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.function.context.PollableBean
import org.springframework.context.annotation.Bean
import org.springframework.integration.support.MessageBuilder
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.Message
import java.util.*
import java.util.function.Consumer
import java.util.function.Function
import java.util.function.Supplier

private val logger = KotlinLogging.logger {}

@SpringBootApplication
class StreamsApplication {

    /* Alternative to  @PollableBean
    @Bean
    fun generate0(): Supplier<Flux<Message<MyModel>>> = Supplier {
        Flux.interval(Duration.ofSeconds(5))
            .map {
                val mdl = MyModel(UUID.randomUUID().toString(), "Paradise", "CA")
                MessageBuilder.withPayload<MyModel>(mdl).setHeader(KafkaHeaders.MESSAGE_KEY, mdl.name).build()
            }.log()
    }
     */


    /**
     * for `Supplier` functions work, we need `spring-cloud-stream-binder-kafka` binder along with
     * `spring-cloud-stream-binder-kafka-streams`
     * you can only use `Consumer` and `Function` with the streams binder.
     */
    @Bean
    fun generate(): Supplier<Message<MyModel>> = Supplier {
        MessageBuilder.withPayload(MyModel(UUID.randomUUID().toString(), "Paradise", "CA"))
            .setHeader(KafkaHeaders.MESSAGE_KEY, UUID.randomUUID().toString())
            .build()
    }

    @Bean
    fun city(): Function<KStream<String, MyModel>, KStream<String, String>> = Function {
        it.peek { k, v -> logger.info("Citi: {}, {}", k, v) }
        it.mapValues { v -> v.city }
    }

    @Bean
    fun state(): Function<KStream<String, MyModel>, KStream<String, String>> = Function {
        it.peek { k, v -> logger.info("State: {}, {}", k, v) }
        it.mapValues { v -> v.state }
    }

    @Bean
    fun print(): Consumer<KStream<String, String>> = Consumer {
        it.peek { k, v -> logger.info("Received: {}, {}", k, v) }
    }

    /*
    // FIXME: Kotlin Lambda support https://github.com/spring-cloud/spring-cloud-function/issues/780
    // waiting for spring-cloud-function 3.2.2
    @PollableBean
    fun generate(): () -> MyModel = {
        MyModel(UUID.randomUUID().toString(), "Paradise", "CA")
    }
    @Bean
    fun city(): (KStream<String, MyModel>) -> KStream<String, String> = {
        it.mapValues { v -> v.city }
    }
    @Bean
    fun state(): (KStream<String, MyModel>) -> KStream<String, String> = {
        it.mapValues { v -> v.state }
    }
    @Bean
    fun print() : (KStream<String, String>) -> Unit = {
        it.peek { k, v -> logger.info("Received: {}, {}", k, v) }
    }
    */
}

fun main(args: Array<String>) {
    runApplication<StreamsApplication>(*args)
}

data class MyModel(
    var name: String? = null,
    var city: String? = null,
    var state: String? = null
)
