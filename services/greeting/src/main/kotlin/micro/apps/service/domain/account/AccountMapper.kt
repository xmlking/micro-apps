package micro.apps.service.domain.account

import micro.apps.service.util.IMapper
import org.mapstruct.InheritInverseConfiguration
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Mapper(componentModel = "cdi")
interface AccountMapper : IMapper<AccountDTO, Account> {
    @Mappings(
        Mapping(source = "phoneNumber", target = "phone"),
        Mapping(source = "firstName", target = "firstName")
    )
    override fun toDTO(entity: Account): AccountDTO

    @InheritInverseConfiguration
    override fun toEntity(dto: AccountDTO): Account
}
