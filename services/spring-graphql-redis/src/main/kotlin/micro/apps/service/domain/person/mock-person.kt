package micro.apps.service.domain.person

import kotlinx.serialization.ExperimentalSerializationApi
import micro.apps.core.dateOf
import micro.apps.model.Gender
import micro.apps.model.Name
import micro.apps.service.domain.account.AddressDto
import micro.apps.service.domain.account.AddressEntity
import micro.apps.service.domain.account.PersonDto
import micro.apps.service.domain.account.PersonEntity
import org.springframework.data.geo.Point
import java.util.Date

// https://medium.com/@june.pravin/better-kotlin-unit-testing-with-mock-helper-functions-38dc1a6c4684

// creates the entire Person graph!
// val batman = mockPersonDto(mockId = 1, dob = 1392249600)
@OptIn(ExperimentalSerializationApi::class)
fun mockPersonDto(
    mockId: Int,
    name: Name = mockName(mockId),
    address: AddressDto = mockAddressDto(mockId),
    gender: Gender = Gender.UNKNOWN,
    dob: Date = dateOf(2014, 1, 11),
    email: String = "user$mockId@gmail.com",
    phone: String = "$mockId$mockId$mockId-4445555",
    avatar: String = "avatar#$mockId"
): PersonDto {
    return PersonDto(
        name = name,
        addresses = setOf(address),
        gender = gender,
        dob = dob,
        email = email,
        phone = phone,
        avatar = avatar
    )
}

// val batmanAddress = mockAddressDto(mockId = 1, city = "Gotham City")
@OptIn(ExperimentalSerializationApi::class)
fun mockAddressDto(
    mockId: Int,
    suite: String = "#$mockId",
    street: String = "first line $mockId",
    city: String = "city$mockId",
    state: String = "state$mockId",
    code: String = "$mockId$mockId$mockId$mockId$mockId",
    country: String = "country$mockId"
): AddressDto {
    return AddressDto(
        suite = suite,
        street = street,
        city = city,
        state = state,
        code = code,
        country = country,
        location = Point(mockId.toDouble(), mockId.toDouble())
    )
}

@OptIn(ExperimentalSerializationApi::class)
fun mockName(
    mockId: Int,
    first: String = "first$mockId",
    last: String = "last$mockId",
    title: String = "title$mockId"
): Name {
    return Name(
        first = first,
        last = last,
        title = title
    )
}

@OptIn(ExperimentalSerializationApi::class)
fun mockPersonList() = listOf(
    PersonEntity(
        name = Name(first = "sumo1", last = "demo1"),
        addresses = setOf(
            AddressEntity(
                id = "a1234",
                suite = "1234",
                street = "Wood Road",
                city = "Riverside",
                state = "California",
                code = "92505",
                country = "CA",
                location = Point(1.1, 1.2)
            )
        ),
        gender = Gender.MALE,
        dob = dateOf(2010, 5, 22),
        email = "sumo1@demo.com",
        phone = "0000000000"
    ),
    PersonEntity(
        name = Name(first = "sumo2", last = "demo2"),
        addresses = setOf(
            AddressEntity(
                suite = "4321",
                street = "Wood Road",
                city = "Riverside",
                state = "California",
                code = "92505",
                country = "CA",
                location = Point(1.1, 1.2)
            )
        ),
        gender = Gender.FEMALE,
        dob = dateOf(2010, 5, 22),
        email = "sumo2@demo.com",
        phone = "1111111111"
    )
)

@OptIn(ExperimentalSerializationApi::class)
fun mockAddressEntity(
    mockId: Int,
    id: String = "id#$mockId",
    suite: String = "#$mockId",
    street: String = "first line $mockId",
    city: String = "city$mockId",
    state: String = "state$mockId",
    code: String = "$mockId$mockId$mockId$mockId$mockId",
    country: String = "country$mockId",
    location: Point = Point(mockId.toDouble(), mockId.toDouble())
): AddressEntity {
    return AddressEntity(
        id = id,
        suite = suite,
        street = street,
        city = city,
        state = state,
        code = code,
        country = country,
        location = location
    )
}

@OptIn(ExperimentalSerializationApi::class)
fun mockPersonEntity(
    mockId: Int,
    id: String = "id#$mockId",
    name: Name = mockName(mockId),
    address: AddressEntity = mockAddressEntity(mockId),
    gender: Gender = Gender.UNKNOWN,
    dob: Date = dateOf(2014, 1, 11),
    email: String = "user$mockId@gmail.com",
    phone: String = "$mockId$mockId$mockId-4445555",
    avatar: String = "avatar#$mockId"
): PersonEntity {
    return PersonEntity(
        id = id,
        name = name,
        addresses = setOf(address),
        gender = gender,
        dob = dob,
        email = email,
        phone = phone,
        avatar = avatar
    )
}
