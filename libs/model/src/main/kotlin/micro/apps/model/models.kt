@file:UseSerializers(LocalDateTimeSerializer::class)

package micro.apps.model

import com.github.avrokotlin.avro4k.Avro
import com.github.avrokotlin.avro4k.AvroAliases
import com.github.avrokotlin.avro4k.AvroDefault
import com.github.avrokotlin.avro4k.AvroEnumDefault
import com.github.avrokotlin.avro4k.AvroFixed
import com.github.avrokotlin.avro4k.AvroProp
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Past
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.Transient
import kotlinx.serialization.UseSerializers
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import kotlinx.serialization.protobuf.ProtoIntegerType
import kotlinx.serialization.protobuf.ProtoNumber
import kotlinx.serialization.protobuf.ProtoType
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.UUID
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
@AvroEnumDefault("UNKNOWN")
enum class Gender {
    UNKNOWN, MALE, FEMALE;

    fun isUnknown(): Boolean = this == UNKNOWN
    fun isMale(): Boolean = this == MALE
    fun isFemale(): Boolean = this == FEMALE
}

@Serializable
@ExperimentalSerializationApi
data class Name(
    @field:NotNull
    @field:Pattern(regexp = "[A-Za-z0-9_]+", message = "FirstName must contain only letters and numbers")
    @field:Size(min = 4, max = 26, message = "FirstName must be between {min} and {max} characters")
    @ProtoNumber(1) @AvroProp("confidential", "true")
    val first: String?,
    @field:NotBlank
    @field:Pattern(regexp = "[A-Za-z0-9_]+", message = "LastName must contain only letters and numbers")
    @ProtoNumber(2)
    val last: String?,
    @ProtoNumber(3) val title: String? = null
)

@Serializable
@ExperimentalSerializationApi
data class Address(
    @ProtoNumber(1) val suite: String? = null,
    @ProtoNumber(2) @AvroProp("confidential", "true")
    val street: String?,
    @ProtoNumber(3) val city: String?,
    @ProtoNumber(4) val state: String?,
    @field:Size(min = 5, max = 15)
    @ProtoNumber(5)
    val code: String?,
    @ProtoNumber(6) val country: String?
    // @ProtoNumber(7) @Contextual val location: Point?
)

@Serializable
@AvroAliases(["account"])
@ExperimentalSerializationApi
data class Person(
    @ProtoNumber(1)@AvroProp("confidential", "true")
    val id: String = "",
    @ProtoNumber(2) @AvroProp("confidential", "true")
    val name: Name,
    @ProtoNumber(3) val addresses: Set<Address>? = setOf(),
    @ProtoNumber(4) @AvroProp("confidential", "true") @AvroDefault("UNKNOWN")
    val gender: Gender,
    @field:Min(value = 18, message = "age must be at least {value}")
    @ProtoNumber(5) @AvroProp("confidential", "true") @ProtoType(ProtoIntegerType.SIGNED)
    val age: Int,
    // @Serializable(with = DateAsLongSerializer::class) // @Polymorphic
    @field:Past(message = "invalid DOB: {value}")
    @ProtoNumber(6) @AvroDefault(Avro.NULL) @AvroProp("confidential", "true")
    val dob: LocalDateTime?,
    @field:Email(message = "Email should be valid")
    @ProtoNumber(7) @AvroProp("encrypted", "yes")
    val email: String? = null,
    @ProtoNumber(8) @AvroProp("encrypted", "yes") @AvroFixed(10)
    val phone: String? = null,
    @ProtoNumber(9) val avatar: String = "https://www.gravatar.com/avatar", // Optional
    @Transient val valid: Boolean = false // not serialized: explicitly transient
)

@Serializable
@ExperimentalSerializationApi
data class MyModel(
    var name: Name? = null,
    @AvroProp("confidential", "true") var city: String? = null,
    var state: String? = null
)

// *** Example  KSerializer for 3rd party classes ***//

@OptIn(ExperimentalSerializationApi::class)
val jsonCodecConfig: Json by lazy {
    Json {
        prettyPrint = true
        // isLenient = true
        ignoreUnknownKeys = true
        serializersModule = modelSerializersModule
        classDiscriminator = "type"
    }
}

@OptIn(ExperimentalSerializationApi::class)
val modelSerializersModule: SerializersModule by lazy {
    SerializersModule {
        contextual(UUIDSerializer)
        // contextual(PointSerializer)
    }
}

object UUIDSerializer : KSerializer<UUID> {
    private val serializer = String.serializer()

    override val descriptor = serializer.descriptor

    override fun serialize(encoder: Encoder, value: UUID) =
        serializer.serialize(encoder, value.toString())

    override fun deserialize(decoder: Decoder) =
        UUID.fromString(serializer.deserialize(decoder))
}

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = Date::class)
object DateAsLongSerializer : KSerializer<Date> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("DateSerializer", PrimitiveKind.LONG)
    override fun serialize(encoder: Encoder, value: Date) = encoder.encodeLong(value.time)
    override fun deserialize(decoder: Decoder): Date = Date(decoder.decodeLong())
}

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = LocalDateTime::class)
object LocalDateTimeSerializer : KSerializer<LocalDateTime> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("LocalDateTimeSerializer", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: LocalDateTime) = encoder.encodeString(DateTimeFormatter.ISO_DATE_TIME.format(value))
    override fun deserialize(decoder: Decoder) = LocalDateTime.parse(decoder.decodeString())

/*
    override fun deserialize(decoder: Decoder): LocalDateTime {
        return ZonedDateTime.parse(decoder.decodeString(), DateTimeFormatter.ISO_DATE_TIME).toLocalDateTime()
    }

    override fun serialize(encoder: Encoder, value: LocalDateTime) {
        encoder.encodeString(
            value.atZone(ZoneId.systemDefault())
                .withFixedOffsetZone()
                .format(DateTimeFormatter.ISO_DATE_TIME)
        )
    }
 */
}

/*
@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = Any::class)
object AnySerializer : KSerializer<Any> {
    override val descriptor: SerialDescriptor
        get() = buildClassSerialDescriptor("kotlin.Any") {
        }

    override fun serialize(encoder: Encoder, value: Any) {
        encoder.encodeStructure(descriptor) {
        }
    }
    override fun deserialize(decoder: Decoder): Any {
        return decoder.decodeStructure(descriptor) {
            Any()
        }
    }
}
*/

/*
@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = Point::class)
object PointSerializer : KSerializer<Point> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("Point") {
        element("x", Double.serializer().descriptor, isOptional = true)
        element("y", Double.serializer().descriptor, isOptional = true)
    }
    override fun serialize(encoder: Encoder, value: Point) =  encoder.encodeStructure(descriptor) {
        encoder.beginStructure(descriptor).apply {
            encodeDoubleElement(descriptor, 0, value.x)
            encodeDoubleElement(descriptor, 1, value.y)
            endStructure(descriptor)
        }

    }
    override fun deserialize(decoder: Decoder): Point = decoder.decodeStructure(descriptor) {
        var x: Double = 0.0
        var y: Double = 0.0
        if (decodeSequentially()) {
            x = decodeDoubleElement(descriptor, 0)
            y = decodeDoubleElement(descriptor, 1)
        } else {
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> x = decodeDoubleElement(descriptor, index)
                    1 -> y = decodeDoubleElement(descriptor, index)
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index: $index")
                }
            }
        }
        Point(x, y)
    }
}
*/
