package micro.apps.core

import com.google.common.flogger.FluentLogger
import com.google.common.flogger.LoggerConfig
import java.util.logging.ConsoleHandler
import java.util.logging.Level

class LogDefinition {

    companion object {
        var DEFAULT_LOG_LEVEL: Level = Level.INFO
        init {
            DEFAULT_LOG_LEVEL = System.getProperty("flogger.level").let {
                when (it) {
                    "OFF" -> Level.OFF
                    "SEVERE" -> Level.SEVERE
                    "WARNING" -> Level.WARNING
                    "CONFIG" -> Level.CONFIG
                    "FINE" -> Level.FINE
                    "FINER" -> Level.FINER
                    "FINEST" -> Level.FINEST
                    "ALL" -> Level.ALL
                    else -> Level.INFO
                }
            }
        }

        fun FluentLogger?.config(lvl: Level = DEFAULT_LOG_LEVEL): FluentLogger {
            with(LoggerConfig.of(this!!)) {
                level = lvl
                addHandler(ConsoleHandler().apply {
                    level = lvl
                })
            }
            return this
        }

        fun jConfig(logger: FluentLogger): FluentLogger {
            return logger.config()
        }
    }
}
