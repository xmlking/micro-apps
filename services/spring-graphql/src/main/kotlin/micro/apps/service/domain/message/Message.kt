package micro.apps.service.domain.message

import com.github.f4b6a3.uuid.UuidCreator
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.reactive.asPublisher
import kotlinx.coroutines.reactor.awaitSingle
import mu.KotlinLogging
import org.reactivestreams.Publisher
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SubscriptionMapping
import org.springframework.stereotype.Controller
import reactor.core.publisher.Mono

data class AddMessageInput(
    var content: String
)

data class Message(
    val id: String,
    val content: String
)

private val logger = KotlinLogging.logger {}

@Controller
class MessageController {
    private val flow = MutableSharedFlow<Message>(replay = 1)

    @QueryMapping
    suspend fun hello(): Message {
        logger.atInfo().log("called hello query")
        return Mono.just(Message(UuidCreator.getTimeOrderedEpoch().toString(), "Hello GQL!"))
            .awaitSingle()
    }

    @MutationMapping
    suspend fun addMessage(@Argument input: AddMessageInput): Message {
        logger.atInfo().log("incoming message: {}", input)
        val message = Message(
            UuidCreator.getTimeOrderedEpoch().toString(),
            input.content
        )
        flow.emit(message)
        return Mono.just(
            message
        ).awaitSingle()
    }

    @SubscriptionMapping
    fun messages(): Publisher<Message> {
        logger.atInfo().log("called messages subscription")
        return flow.asSharedFlow().asPublisher()
    }

//    @SubscriptionMapping
//    fun messages(): Flux<Message> {
//        return Flux.range(0, Int.MAX_VALUE).map { Message(UuidCreator.getTimeOrderedEpoch().toString(), "$it") } .delayElements(Duration.ofSeconds(1))
//    }
}
