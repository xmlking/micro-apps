package micro.apps.core.util;

import com.google.common.flogger.FluentLogger;
import com.google.common.flogger.LoggerConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class LogJavaTest {
    private static final FluentLogger logger = LogDefinition.Companion.jConfig(FluentLogger.forEnclosingClass());

    @Test
    public void testLogger() {
        LoggerConfig config = LoggerConfig.of(logger);
        logger.atSevere().log("******** atSevere MESSAGE");
        logger.atFinest().log("******** atFinest MESSAGE");
        logger.atFine().log("******** atFine MESSAGE");
        logger.atWarning().log("******** atWarning MESSAGE");
        logger.atInfo().log("******** atInfo MESSAGE");
        assertEquals(config.getName(), LogJavaTest.class.getName());
    }
}
