package micro.apps.service.domain.account

import micro.apps.service.util.AbstractService
import micro.apps.service.util.Result
import micro.apps.service.util.Success
import micro.apps.service.util.toDTOs
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

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
