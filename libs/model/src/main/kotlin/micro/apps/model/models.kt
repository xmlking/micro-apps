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
@AvroProp("mode", "private")
data class Person(
    @AvroProp("pii", "yes") @ProtoId(1) val id: String = "",
    @ProtoId(2) val firstName: String,
    @ProtoId(3) val lastName: String,
    @AvroProp("pii", "yes") @AvroFixed(10) @ProtoId(4) val phone: String,
    @AvroProp("encrypted", "yes") @ProtoId(5) val email: String,
    @AvroProp("pii", "yes") @ProtoId(6) @ProtoType(ProtoNumberType.SIGNED) val age: Int,
    @Transient val valid: Boolean = false // not serialized: explicitly transient
)
