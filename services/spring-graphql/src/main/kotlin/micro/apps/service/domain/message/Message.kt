package micro.apps.service.domain.message

import com.github.f4b6a3.uuid.UuidCreator
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.reactive.asPublisher
import kotlinx.coroutines.reactor.awaitSingle
import micro.apps.service.util.jwtAuthentication
import mu.KotlinLogging
import org.reactivestreams.Publisher
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SubscriptionMapping
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Controller
import reactor.core.publisher.Mono
import java.security.Principal

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
    @PreAuthorize("hasAuthority('SCOPE_editor')")
    suspend fun hello(principal: Principal): Message {
        logger.atDebug().log("called hello query, principal: {}", principal)
        return Mono.just(Message(UuidCreator.getTimeOrderedEpoch().toString(), "Hello ${principal.name}!"))
            .awaitSingle()
    }

    @MutationMapping
    @PreAuthorize("hasAuthority('SCOPE_editor')")
    suspend fun addMessage(principal: Principal, @Argument input: AddMessageInput): Message {
        logger.atDebug().log("incoming message: {}", input)
        val claims = principal.jwtAuthentication().token.claims
        logger.atDebug().log("called addMessage, claims: {}, sub: {}", claims, claims["sub"])
        val message = Message(
            UuidCreator.getTimeOrderedEpoch().toString(),
            input.content + claims["sub"]
        )
        flow.emit(message)
        return Mono.just(
            message
        ).awaitSingle()
    }

    @SubscriptionMapping
    fun messages(@AuthenticationPrincipal jwt: Jwt): Publisher<Message> {
        logger.atDebug().log("called messages subscription, jwt: {}", jwt)
        return flow.asSharedFlow().asPublisher()
    }

//    @SubscriptionMapping
//    fun messages(): Flux<Message> {
//        return Flux.range(0, Int.MAX_VALUE).map { Message(UuidCreator.getTimeOrderedEpoch().toString(), "$it") } .delayElements(Duration.ofSeconds(1))
//    }
}
