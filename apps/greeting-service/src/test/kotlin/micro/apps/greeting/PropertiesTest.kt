package micro.apps.greeting

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.quarkus.test.junit.QuarkusTest
import org.eclipse.microprofile.config.inject.ConfigProperty

@QuarkusTest
class PropertiesTest : FunSpec() {

    @ConfigProperty(name = "test-foo")
    lateinit var testFoo: String

    init {
        test("Should load properties correctly") {
            testFoo shouldBe "bar"
        }
    }
}
