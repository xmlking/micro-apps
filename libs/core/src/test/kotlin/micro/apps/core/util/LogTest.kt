package micro.apps.core.util

import com.google.common.flogger.FluentLogger
import com.google.common.flogger.LoggerConfig
import com.google.common.flogger.testing.FakeLoggerBackend
import java.util.logging.Level
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import micro.apps.core.util.LogDefinition.Companion.config

class LogTest {
    private var logger: FluentLogger = FluentLogger.forEnclosingClass().config(Level.ALL)
    private lateinit var backend: FakeLoggerBackend

    @BeforeTest
    fun setUpLogger() {
        backend = FakeLoggerBackend("micro.apps.core.util.LogTest")
        backend.setLevel(Level.ALL)
        // we need to make FluentLogger's constructor accessible, as it is private
        // logger = FluentLogger(backend)
        val constructor = FluentLogger::class.java.declaredConstructors[0]
        constructor.isAccessible = true
        logger = constructor.newInstance(backend) as FluentLogger // FluentLogger(backend)
    }

    @Test
    fun `log level should be enabled for all`() {
        assertTrue(logger.atFine().isEnabled)
        assertTrue(logger.atSevere().isEnabled)
    }

    @Test
    fun `logger name should match to class`() {
        val config = LoggerConfig.of(logger)
        assertEquals(config.name, LogTest::class.java.name)
    }

    @Test
    fun `log should output`() {
        logger.atSevere().log("Severe MESSAGE")
        backend.assertLastLogged().hasMessage("Severe MESSAGE")
        logger.atFinest().log("Finest MESSAGE")
        backend.assertLastLogged().hasMessage("Finest MESSAGE")
        logger.atFine().log("Fine MESSAGE")
        backend.assertLastLogged().hasMessage("Fine MESSAGE")
        logger.atWarning().log("Warning MESSAGE")
        backend.assertLastLogged().hasMessage("Warning MESSAGE")
        logger.atInfo().log("Info MESSAGE")
        backend.assertLastLogged().hasMessage("Info MESSAGE")
    }
}
