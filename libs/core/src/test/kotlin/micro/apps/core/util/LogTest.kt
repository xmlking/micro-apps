package micro.apps.core.util

import com.google.common.flogger.FluentLogger
import micro.apps.core.util.LogDefinition.Companion.config
import java.util.logging.Level
import kotlin.test.Test

class LogTest {
    private var logger: FluentLogger = FluentLogger.forEnclosingClass().config(Level.ALL)

    @Test
    fun `log test`() {
        logger.atSevere().log("******** atSevere MESSAGE")
        logger.atFinest().log("******** atFinest MESSAGE")
        logger.atFine().log("******** atFine MESSAGE")
        logger.atWarning().log("******** atWarning MESSAGE")
        logger.atInfo().log("******** atInfo MESSAGE")
    }
}
