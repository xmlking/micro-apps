package micro.apps.chat.service

import kotlinx.coroutines.flow.Flow

interface MessageService {

    fun latest(): Flow<MessageVM>

    fun after(messageId: String): Flow<MessageVM>

    fun stream(): Flow<MessageVM>

    suspend fun post(messages: Flow<MessageVM>)
}
