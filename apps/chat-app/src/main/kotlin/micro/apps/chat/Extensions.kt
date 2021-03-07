package micro.apps.chat

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import micro.apps.chat.repository.ContentType
import micro.apps.chat.repository.Message
import micro.apps.chat.service.MessageVM
import micro.apps.chat.service.UserVM
import org.intellij.markdown.flavours.commonmark.CommonMarkFlavourDescriptor
import org.intellij.markdown.html.HtmlGenerator
import org.intellij.markdown.parser.MarkdownParser
import java.net.URL

fun MessageVM.asDomainObject(contentType: ContentType = micro.apps.chat.repository.ContentType.MARKDOWN): Message =
    Message(
        content,
        contentType,
        sent,
        user.name,
        user.avatarImageLink.toString(),
        id
    )

fun Message.asViewModel(): MessageVM = MessageVM(
    contentType.render(content),
    UserVM(username, URL(userAvatarImageLink)),
    sent,
    id
)

fun MessageVM.asRendered(contentType: ContentType = micro.apps.chat.repository.ContentType.MARKDOWN): MessageVM =
    this.copy(content = contentType.render(this.content))

fun Flow<Message>.mapToViewModel(): Flow<MessageVM> = map { it.asViewModel() }

fun List<Message>.mapToViewModel(): List<MessageVM> = map { it.asViewModel() }

fun ContentType.render(content: String): String = when (this) {
    ContentType.PLAIN -> content
    ContentType.MARKDOWN -> {
        val flavour = CommonMarkFlavourDescriptor()
        HtmlGenerator(content, MarkdownParser(flavour).buildMarkdownTreeFromString(content), flavour).generateHtml()
    }
}
