package micro.apps.service.util

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import micro.apps.core.capitalize
import java.util.Locale

class ExtensionsTest : FunSpec({
    test("capitalize string with locale") {
        val sumo = "sumo".capitalize(Locale.Builder().setLanguage("en").setRegion("US").build())
        sumo shouldBe "Sumo"
    }
})
