package micro.apps.model.fixtures

import micro.apps.model.Address
import micro.apps.model.Gender
import micro.apps.model.Name
import micro.apps.model.Person

// https://medium.com/@june.pravin/better-kotlin-unit-testing-with-mock-helper-functions-38dc1a6c4684

// creates the entire Person graph!
// val batman = mockPerson(mockId = 1, age = 44)
fun mockPerson(
    mockId: Int,
    id: String = "id#$mockId",
    name: Name = mockName(mockId),
    address: Address = mockAddress(mockId),
    gender: Gender = Gender.UNKNOWN,
    age: Int = mockId,
    email: String = "user$mockId@gmail.com",
    phone: String = "$mockId$mockId$mockId",
    avatar: String = "avatar#$mockId"
): Person {
    return Person(
        id = id,
        name = name,
        address = address,
        gender = gender,
        age = age,
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
