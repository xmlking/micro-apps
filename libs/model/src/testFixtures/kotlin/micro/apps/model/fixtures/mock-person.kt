package micro.apps.model.fixtures

import micro.apps.model.Address
import micro.apps.model.Gender
import micro.apps.model.Name
import micro.apps.model.Person
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField

// https://medium.com/@june.pravin/better-kotlin-unit-testing-with-mock-helper-functions-38dc1a6c4684

val fmt: DateTimeFormatter = DateTimeFormatterBuilder()
    .appendPattern("yyyy-MM-dd")
    .optionalStart()
    .appendPattern(" HH:mm")
    .optionalEnd()
    .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
    .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
    .toFormatter()

// creates the entire Person graph!
// val batman = mockPerson(mockId = 1, age = 44)
fun mockPersonDto(
    mockId: Int,
    id: String = "id#$mockId",
    name: Name = mockName(mockId),
    address: Address = mockAddress(mockId),
    gender: Gender = Gender.UNKNOWN,
    age: Int = mockId,
    dob: LocalDateTime = LocalDateTime.parse("1999-05-30", fmt),
    email: String = "user$mockId@gmail.com",
    phone: String = "$mockId$mockId$mockId",
    avatar: String = "avatar#$mockId"
): Person {
    return Person(
        id = id,
        name = name,
        addresses = setOf(address),
        gender = gender,
        age = age,
        dob = dob,
        email = email,
        phone = phone,
        avatar = avatar
    )
}

// val batmanAddress = mockAddress(mockId = 1, city = "Gotham City")
fun mockAddress(
    mockId: Int,
    suite: String = "#$mockId",
    street: String = "first line $mockId",
    city: String = "city$mockId",
    state: String = "state$mockId",
    code: String = "$mockId",
    country: String = "country$mockId"
): Address {
    return Address(
        suite = suite,
        street = street,
        city = city,
        state = state,
        code = code,
        country = country
    )
}

fun mockName(
    mockId: Int,
    first: String = "first $mockId",
    last: String = "last $mockId",
    title: String = "title $mockId"
): Name {
    return Name(
        first = first,
        last = last,
        title = title
    )
}

fun mockPersonList() = listOf(
    Person(
        name = Name(first = "sumo1", last = "demo1"),
        addresses = setOf(
            Address(
                suite = "1234",
                street = "Wood Road",
                city = "Riverside",
                state = "California",
                code = "92505",
                country = "CA"
            )
        ),
        dob = LocalDateTime.parse("1999-05-30", fmt),
        gender = Gender.MALE, age = 99,
        email = "sumo1@demo.com", phone = "0000000000"
    ),
    Person(
        name = Name(first = "sumo2", last = "demo2"),
        addresses = setOf(
            Address(
                suite = "4321",
                street = "Wood Road",
                city = "Riverside",
                state = "California",
                code = "92505",
                country = "CA"
            )
        ),
        dob = LocalDateTime.parse("1999-05-30", fmt),
        gender = Gender.FEMALE, age = 99,
        email = "sumo2@demo.com", phone = "1111111111"
    )
)
