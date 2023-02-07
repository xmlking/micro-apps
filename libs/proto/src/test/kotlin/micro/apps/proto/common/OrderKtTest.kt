package micro.apps.proto.common

import com.google.protobuf.StringValue
import io.envoyproxy.pgv.ReflectiveValidatorIndex
import io.envoyproxy.pgv.ValidationException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import micro.apps.proto.common.v1.Currency
import micro.apps.proto.common.v1.Order
import micro.apps.proto.common.v1.order

class OrderKtTest : FunSpec({

    val index = ReflectiveValidatorIndex()
    val validator = index.validatorFor<Order>(Order::class.java)

    test("Order proto generated class should be validated") {
        val order = order {
            subject = StringValue.newBuilder().setValue("sumo-demo").build()
            totalPrice = 52
            currency = Currency.CURRENCY_USD_UNSPECIFIED
        }

        validator.assertValid(order)
        order.subject.value shouldBe "sumo-demo"
        order.body.isEmpty() shouldBe true
        order.totalPrice shouldBe 52
    }

    test("Order proto generated class should be fail with invalid subject") {
        val order = order {
            subject = StringValue.newBuilder().setValue("demo").build()
            totalPrice = 52
            currency = Currency.CURRENCY_USD_UNSPECIFIED
        }

        val exception = shouldThrow<ValidationException> {
            validator.assertValid(order)
        }
        exception.message shouldBe ".micro.apps.proto.common.v1.Order.subject: length must be at least 5 but got: 4 - Got \"demo\""

        order.subject.value shouldBe "demo"
        order.body.isEmpty() shouldBe true
        order.totalPrice shouldBe 52
    }
})
