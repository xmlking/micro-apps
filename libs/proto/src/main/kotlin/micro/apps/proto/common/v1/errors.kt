package micro.apps.proto.common.v1

typealias PersonId = String
typealias ProductId = String
typealias OrderId = String

class DuplicateCustomerIdException(personId: PersonId) : RuntimeException("A person with id $personId already exist")
class DuplicateProductIdException(productId: ProductId) : RuntimeException("A product with id $productId already exist")
class DuplicateOrderIdException(orderId: OrderId) : RuntimeException("An order with id $orderId already exist")
