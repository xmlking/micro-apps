package micro.apps.model

import com.sksamuel.avro4k.AvroFixed
import com.sksamuel.avro4k.AvroProp
import java.io.Serializable as JavaSerializable
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID
import kotlinx.serialization.SerialInfo
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.protobuf.ProtoNumberType
import kotlinx.serialization.protobuf.ProtoType

// ----------------
//  User-defined serial annotation for ProtoBuf
// ----------------
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
annotation class ProtoId(val id: Int)

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

@Serializable
@AvroProp("pii", "yes")
data class Name(
    @ProtoId(1) val first: String,
    @ProtoId(2) val last: String,
    @ProtoId(3) val title: String = ""
)

@Serializable
@AvroProp("pii", "yes")
data class Address(
    @ProtoId(1) val suite: String,
    @ProtoId(2) val street: String,
    @ProtoId(3) val city: String,
    @ProtoId(4) val state: String,
    @ProtoId(5) val code: String,
    @ProtoId(6) val country: String
)

@Serializable
data class Person(
    @ProtoId(1) @AvroProp("pii", "yes") val id: String = "",
    @ProtoId(2) val name: Name,
    @ProtoId(3) val address: Address,
    @ProtoId(4) @AvroProp("pii", "yes") val gender: Gender,
    @ProtoId(5) @AvroProp("pii", "yes") @ProtoType(ProtoNumberType.SIGNED) val age: Int,
    @ProtoId(6) @AvroProp("encrypted", "yes") val email: String,
    @ProtoId(7) @AvroProp("encrypted", "yes") @AvroFixed(10) val phone: String,
    @ProtoId(8) val avatar: String = "http://www.gravatar.com/avatar", // Optional
    @Transient val valid: Boolean = false // not serialized: explicitly transient
)
