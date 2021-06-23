package micro.apps.service.controller

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.onStart
import micro.apps.service.service.MessageService
import micro.apps.service.service.MessageVM
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Controller

@Controller
@MessageMapping("api.v1.messages")
class MessageResource(val messageService: MessageService) {

    @MessageMapping("stream")
    suspend fun receive(@Payload inboundMessages: Flow<MessageVM>) =
        messageService.post(inboundMessages)

    @MessageMapping("stream")
    fun send(): Flow<MessageVM> = messageService
        .stream()
        .onStart {
            emitAll(messageService.latest())
        }
}
