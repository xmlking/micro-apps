package micro.apps.pipeline

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class UtilsTest : FunSpec({
    test("Dum should add") {
        sum(5, 4) shouldBe 9
    }
})
