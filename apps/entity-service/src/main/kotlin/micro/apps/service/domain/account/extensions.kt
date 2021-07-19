package micro.apps.service.domain.account

import kotlinx.serialization.ExperimentalSerializationApi

@OptIn(ExperimentalSerializationApi::class)
fun PersonDto.toEntity(): PersonEntity {
    val addresses = this.addresses?.map { it.toEntity() }?.toSet()
    return PersonEntity(null, name, addresses, gender, age, email, phone, avatar)
}

@OptIn(ExperimentalSerializationApi::class)
fun AddressDto.toEntity() =
    AddressEntity(null, suite, street, city, state, code, country, location)
