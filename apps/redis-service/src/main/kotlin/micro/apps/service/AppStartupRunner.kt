package micro.apps.service

import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import micro.apps.service.domain.account.AccountService
import mu.KotlinLogging
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component


private val logger = KotlinLogging.logger {}

@Profile("!test")
@Component
class AppStartupRunner( private val accountService: AccountService) : ApplicationRunner {

    @OptIn(ExperimentalSerializationApi::class)
    @Throws(Exception::class)
    override fun run(args: ApplicationArguments) {
        logger.info("Your application started with option names : {}", args.optionNames)
        runBlocking {
            val batman = mockPersonDto(mockId = 1)
            val batmanEnt = accountService.createPerson(batman)
            println(batmanEnt)
        }

    }
}
