package micro.apps.demo

import com.google.common.flogger.FluentLogger
import micro.apps.dlib.Library

class App {
    val greeting: String
        get() {
            return "Hello world."
        }
}

private val logger = FluentLogger.forEnclosingClass()

fun main(args: Array<String>) {
    // logger.atInfo().withCause(exception).log("Log message with: %s", argument);

    logger.atInfo().log("greeting: %s, lib: %b", App().greeting, Library().someLibraryMethod())
}
