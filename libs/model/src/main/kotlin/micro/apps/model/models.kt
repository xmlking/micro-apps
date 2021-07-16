package micro.apps.model

import com.sksamuel.avro4k.AvroFixed
import com.sksamuel.avro4k.AvroProp
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.protobuf.ProtoIntegerType
import kotlinx.serialization.protobuf.ProtoNumber
import kotlinx.serialization.protobuf.ProtoType
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID
import javax.validation.constraints.Email
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size
import java.io.Serializable as JavaSerializable

// ----------------
//  for Kotlin Gradle NoArg plugin
// ----------------
annotation class NoArg

@NoArg
class Test(val id: Int)

sealed class Message : JavaSerializable

/** Event Message Class **/
data class DataMessage(
    val id: UUID,
    val name: String,
    val location: String = "CA",
    val price: BigDecimal,
    val timestamp: Instant = Instant.now()
) : Message(), JavaSerializable

data class Fruit(var name: String = "", var description: String = "")

data class Greeting(val message: String = "")

@Serializable
enum class Gender {
    UNKNOWN, MALE, FEMALE;

    fun isUnknown(): Boolean = this == UNKNOWN
    fun isMale(): Boolean = this == MALE
    fun isFemale(): Boolean = this == FEMALE
}

@ExperimentalSerializationApi
@Serializable
@AvroProp("pii", "yes")
data class Name(
    @NotNull
    @ProtoNumber(1) val first: String,
    @NotNull
    @ProtoNumber(2) val last: String,
    @ProtoNumber(3) val title: String? = null
)

@ExperimentalSerializationApi
@Serializable
@AvroProp("pii", "yes")
data class Address(
    @ProtoNumber(1) val suite: String?,
    @ProtoNumber(2) val street: String?,
    @ProtoNumber(3) val city: String?,
    @ProtoNumber(4) val state: String?,
    @Size(min = 5, max = 15)
    @ProtoNumber(5) val code: String?,
    @ProtoNumber(6) val country: String?,
    // @ProtoNumber(7) @Contextual val location: Point?
)

@Serializable
@kotlinx.serialization.ExperimentalSerializationApi
data class Person(
    @ProtoNumber(1) @AvroProp("pii", "yes") val id: String = "",
    @ProtoNumber(2) val name: Name,
    @ProtoNumber(3) val addresses: Set<Address>? = setOf(),
    @ProtoNumber(4) @AvroProp("pii", "yes") val gender: Gender,
    @Min(value = 18, message = "age must be at least {value}")
    @ProtoNumber(5) @AvroProp("pii", "yes") @ProtoType(ProtoIntegerType.SIGNED) val age: Int,
    @Email(message = "Email should be valid")
    @ProtoNumber(6) @AvroProp("encrypted", "yes") val email: String,
    @ProtoNumber(7) @AvroProp("encrypted", "yes") @AvroFixed(10) val phone: String,
    @ProtoNumber(8) val avatar: String = "https://www.gravatar.com/avatar", // Optional
    @Transient val valid: Boolean = false // not serialized: explicitly transient
)

typealias PersonId = String

/*** Exceptions ***/
class DuplicateCustomerIdException(personId: PersonId) : RuntimeException("A person with id $personId already exist")
class PersonNotFoundException(override val message: String): Exception(message)
class AddressNotFoundException(override val message: String): Exception(message)

