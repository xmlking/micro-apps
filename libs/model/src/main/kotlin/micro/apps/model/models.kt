package micro.apps.model

import com.sksamuel.avro4k.AvroFixed
import com.sksamuel.avro4k.AvroProp
import java.io.Serializable as JavaSerializable
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.protobuf.ProtoIntegerType
import kotlinx.serialization.protobuf.ProtoNumber
import kotlinx.serialization.protobuf.ProtoType

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
    @ProtoNumber(1) val first: String,
    @ProtoNumber(2) val last: String,
    @ProtoNumber(3) val title: String = ""
)

@ExperimentalSerializationApi
@Serializable
@AvroProp("pii", "yes")
data class Address(
    @ProtoNumber(1) val suite: String,
    @ProtoNumber(2) val street: String,
    @ProtoNumber(3) val city: String,
    @ProtoNumber(4) val state: String,
    @ProtoNumber(5) val code: String,
    @ProtoNumber(6) val country: String
)

@Serializable
@kotlinx.serialization.ExperimentalSerializationApi
data class Person(
    @ProtoNumber(1) @AvroProp("pii", "yes") val id: String = "",
    @ProtoNumber(2) val name: Name,
    @ProtoNumber(3) val address: Address,
    @ProtoNumber(4) @AvroProp("pii", "yes") val gender: Gender,
    @ProtoNumber(5) @AvroProp("pii", "yes") @ProtoType(ProtoIntegerType.SIGNED) val age: Int,
    @ProtoNumber(6) @AvroProp("encrypted", "yes") val email: String,
    @ProtoNumber(7) @AvroProp("encrypted", "yes") @AvroFixed(10) val phone: String,
    @ProtoNumber(8) val avatar: String = "http://www.gravatar.com/avatar", // Optional
    @Transient val valid: Boolean = false // not serialized: explicitly transient
)
