package micro.apps.greeting

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.quarkus.test.junit.QuarkusTest
import micro.apps.greeting.domain.account.AccountDTO
import micro.apps.greeting.domain.account.AccountService
import micro.apps.greeting.util.Failure
import micro.apps.greeting.util.Success
import javax.inject.Inject

@QuarkusTest
class AccountServiceTest : FunSpec() {

    @Inject
    lateinit var accountService: AccountService

    init {
        test("user service should be injected") {
            val result = accountService.findById("sumo")
            if (result is Failure<AccountDTO>) {
                // result.left shouldBe
            } else if (result is Success<AccountDTO>) {
                result.right.firstName shouldBe "firstName-sumo"
            }
        }
    }
}
