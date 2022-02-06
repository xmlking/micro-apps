package micro.apps.service

import com.redislabs.lettusearch.CreateOptions
import com.redislabs.lettusearch.Field
import com.redislabs.lettusearch.RediSearchAsyncCommands
import com.redislabs.lettusearch.StatefulRediSearchConnection
import com.redislabs.lettusearch.Suggestion
import mu.KotlinLogging
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import java.io.IOException

private val logger = KotlinLogging.logger {}

@Profile("!test")
@Component
class AppStartupRunner(private val conn: StatefulRediSearchConnection<String, String>) : ApplicationRunner {

    @Throws(Exception::class)
    override fun run(args: ApplicationArguments) {
        logger.info("Your application started with option names : {}", args.optionNames)

        val ABV = "abv"
        val ID = "id"
        val NAME = "name"
        val STYLE = "style"
        val INDEX = "beers"
        val SUGINDEX = "beersSug"

        val searchIndex = "articles-idx"

        val commands: RediSearchAsyncCommands<String, String> = conn.async()
        try {
            commands.create(
                "beers",
                CreateOptions.builder<String, String>().prefix("customer:").temporary(1L).build(),
                Field.text("name").weight(1.0).noStem(true).sortable(true).build(),
                Field.text("subtitle").weight(1.0).noStem(true).build(),
                Field.numeric("ibu").build(),

                Field.text("addresses.[1]").build(),
                Field.text("addresses.[2]").build(),
                Field.text("addresses.[3]").build(),
                Field.text("addresses.[4]").build(),
                Field.text("addresses.[5]").build(),
                Field.text("addresses.[6]").build()
            )
        } catch (lde: IOException) {
            // ignore - index already exists
        }

        try {
            commands.create(
                INDEX, CreateOptions.builder<String, String>().payloadField(NAME).build(),
                Field.text(NAME).matcher(Field.Text.PhoneticMatcher.English).build(),
                Field.tag(STYLE).sortable(true).build(),
                Field.numeric(ABV).sortable(true).build()
            )
            conn.setAutoFlushCommands(false)
            conn.flushCommands()
            conn.setAutoFlushCommands(true)
        } catch (lde: IOException) {
            // ignore - index already exists
        }

        try {
            conn.setAutoFlushCommands(false)
            commands.sugadd(SUGINDEX, Suggestion.builder("authorName").score(1.0).build(), false)
            conn.flushCommands()
            conn.setAutoFlushCommands(true)
        } catch (lde: IOException) {
            // ignore - index already exists
        }
    }
}
