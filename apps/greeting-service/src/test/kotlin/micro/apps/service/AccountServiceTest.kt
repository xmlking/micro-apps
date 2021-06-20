package micro.apps.service

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.quarkus.test.junit.QuarkusTest
import micro.apps.service.domain.account.AccountDTO
import micro.apps.service.domain.account.AccountService
import micro.apps.service.util.Failure
import micro.apps.service.util.Success
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
