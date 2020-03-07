package micro.apps.core.util

import java.time.Instant
import kotlin.test.Test
import kotlin.test.assertEquals

class ExtensionsTest {
    @Test
    fun `Test toISO8601 extension of Instant`() {
        val timestamp: Instant = Instant.parse("1995-10-23T10:12:35Z");
        assertEquals(timestamp.toISO8601(), "1995-10-23T10:12:35.000Z")
    }
}