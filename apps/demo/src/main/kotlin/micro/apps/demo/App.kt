package micro.apps.demo

import com.google.common.flogger.FluentLogger

class App {
    val greeting: String
        get() {
            return "Hello world."
        }
}

private val logger = FluentLogger.forEnclosingClass()

fun main(args: Array<String>) {
    // logger.atInfo().withCause(exception).log("Log message with: %s", argument);
    logger.atInfo().log("greeting: %s", App().greeting);
}
