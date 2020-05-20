package micro.apps.core

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import java.time.Instant

class ExtensionsTest : FunSpec({
    test("Test toISO8601 extension of Instant") {
        val timestamp: Instant = Instant.parse("1995-10-23T10:12:35Z")
        timestamp.toISO8601() shouldBe "1995-10-23T10:12:35.000Z"
    }
})
