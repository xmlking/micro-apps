package micro.apps.proto.common.fixtures

import com.google.protobuf.StringValue
import micro.apps.proto.common.v1.Currency
import micro.apps.proto.common.v1.Order
import micro.apps.proto.common.v1.Person
import micro.apps.proto.common.v1.Product

// creates the entire Person graph!
// val batman = mockPerson(mockId = 1, lastName = "demo")
fun mockPerson(
    mockId: Int,
    id: String = "id#$mockId",
    firstName: String = "firstName$mockId",
    lastName: String = "lastName$mockId",
    phone: String = "$mockId$mockId$mockId",
    email: String = "user$mockId@gmail.com"
): Person {
    return with(Person.newBuilder()) {
        this.id = id
        this.firstName = firstName
        this.lastName = lastName
        this.phone = phone
        this.email = email
        return@with build()
    }
}

fun mockProduct(
    mockId: Int,
    id: String = "id#$mockId",
    slug: String = "slug$mockId",
    description: String = "description$mockId",
    currency: Currency = Currency.CURRENCY_USD_UNSPECIFIED,
    price: Int = mockId
): Product {
    return with(Product.newBuilder()) {
        this.id = id
        this.slug = slug
        this.description = description
        this.currency = currency
        this.price = price
        return@with build()
    }
}

fun mockOrder(
    mockId: Int,
    id: String = "id#$mockId",
    subject: String = "subject#$mockId",
    body: String = "body$mockId",
    currency: Currency = Currency.CURRENCY_USD_UNSPECIFIED,
    totalPrice: Int = mockId
): Order {
    return with(Order.newBuilder()) {
        this.id = id
        this.subject = StringValue.newBuilder().setValue(subject).build()
        this.body = body
        this.currency = currency
        this.totalPrice = totalPrice
        return@with build()
    }
}
