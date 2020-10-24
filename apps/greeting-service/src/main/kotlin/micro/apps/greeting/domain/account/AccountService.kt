package micro.apps.greeting.domain.account

import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import micro.apps.greeting.util.AbstractService
import micro.apps.greeting.util.Result
import micro.apps.greeting.util.Success
import micro.apps.greeting.util.toDTOs

@ApplicationScoped
class AccountService : AbstractService<AccountDTO, Account>() {

    @Inject
    override lateinit var mapper: AccountMapper

    override fun findById(id: String): Result<AccountDTO> {
        return Success(mapper.toDTO(Account(firstName = "firstName-$id", phoneNumber = "lastName-$id")))
    }

    override fun findAll(index: Int, size: Int): List<AccountDTO> {
        return mapper.toDTOs(listOf<Account>(Account(firstName = "firstName-$index", phoneNumber = "lastName-$index")))
    }

    override fun save(dto: AccountDTO): Result<AccountDTO> {
        TODO("Not yet implemented")
    }

    override fun delete(dto: AccountDTO): Exception? {
        TODO("Not yet implemented")
    }
}
