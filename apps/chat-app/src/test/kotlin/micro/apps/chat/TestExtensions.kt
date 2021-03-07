package micro.apps.chat

import micro.apps.chat.repository.Message
import micro.apps.chat.service.MessageVM
import java.time.temporal.ChronoUnit.MILLIS

fun MessageVM.prepareForTesting() = copy(id = null, sent = sent.truncatedTo(MILLIS))

fun Message.prepareForTesting() = copy(id = null, sent = sent.truncatedTo(MILLIS))
