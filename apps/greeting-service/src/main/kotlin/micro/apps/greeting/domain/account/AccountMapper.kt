package micro.apps.greeting.domain.account

import javax.enterprise.context.ApplicationScoped
import micro.apps.greeting.util.IMapper

// import org.mapstruct.Mapper;
//
// @Mapper(componentModel = "cdi", uses = {})
@ApplicationScoped
class AccountMapper : IMapper<AccountDTO, Account> {
    override fun toDto(entity: Account): AccountDTO {
        return AccountDTO(
            name = entity.name
        )
    }

    override fun toEntity(dto: AccountDTO): Account {
        // TODO("Not yet implemented")
        return Account(
            name = dto.name
        )
    }
}
