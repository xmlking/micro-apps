package micro.apps.greeting

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.quarkus.test.junit.QuarkusTest
import micro.apps.greeting.domain.greeting.GreetingService
import javax.inject.Inject

@QuarkusTest
class MockTest : FunSpec() {

    @Inject
    lateinit var greetingService: GreetingService

    init {
        test("Should inject the mocked bean") {
            greetingService.greeting("sumo") shouldBe "Welcome"
        }
    }
}
