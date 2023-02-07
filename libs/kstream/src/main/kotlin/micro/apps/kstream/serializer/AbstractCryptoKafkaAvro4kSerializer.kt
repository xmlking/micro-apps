package micro.apps.kstream.serializer

import com.github.avrokotlin.avro4k.Avro
import com.github.avrokotlin.avro4k.io.AvroEncodeFormat
import com.github.thake.kafka.avro4k.serializer.Avro4kSchemaUtils
import io.confluent.kafka.serializers.NonRecordContainer
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer
import org.apache.avro.Schema
import org.apache.avro.generic.GenericDatumWriter
import org.apache.avro.io.EncoderFactory
import org.apache.kafka.common.errors.SerializationException
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.nio.ByteBuffer

abstract class AbstractCryptoKafkaAvro4kSerializer : AbstractCryptoKafkaAvro4kSerDe() {
    private var autoRegisterSchema = false

    protected val avroSchemaUtils = Avro4kSchemaUtils()
    protected fun configure(config: CryptoKafkaAvro4kSerializerConfig) {
        autoRegisterSchema = config.autoRegisterSchema()
        super.configure(config)
    }

    protected fun serializerConfig(props: Map<String, *>): CryptoKafkaAvro4kSerializerConfig {
        return CryptoKafkaAvro4kSerializerConfig(props)
    }

    @Throws(SerializationException::class)
    protected fun serializeImpl(subject: String?, obj: Any?): ByteArray? {
        return if (obj == null) {
            null
        } else {
            try {
                val currentSchema = avroSchemaUtils.getSchema(obj)
                val id = getSchemaId(subject, currentSchema)
                val out = ByteArrayOutputStream()
                writeSchemaId(out, id)
                if (obj is ByteArray) {
                    out.write(obj)
                } else {
                    serializeValue(out, obj, currentSchema)
                }
                val bytes = out.toByteArray()
                out.close()
                bytes
            } catch (re: RuntimeException) {
                throw SerializationException("Error serializing Avro message with avro4k", re)
            } catch (io: IOException) {
                throw SerializationException("Error serializing Avro message with avro4k", io)
            }
        }
    }

    @OptIn(InternalSerializationApi::class)
    fun serializeValue(out: ByteArrayOutputStream, obj: Any, currentSchema: Schema) {
        val value =
            if (obj is NonRecordContainer) obj.value else obj
        if (currentSchema.type == Schema.Type.RECORD) {
            @Suppress("UNCHECKED_CAST")
            Avro.default.openOutputStream(value::class.serializer() as KSerializer<Any>) {
                encodeFormat = AvroEncodeFormat.Binary
                schema = currentSchema
            }.to(out).write(obj).close()
        } else {
            val encoder = EncoderFactory.get().directBinaryEncoder(out, null)
            val datumWriter = GenericDatumWriter<Any>(currentSchema)
            datumWriter.write(obj, encoder)
            encoder.flush()
        }
    }

    private fun writeSchemaId(out: ByteArrayOutputStream, id: Int) {
        out.write(0)
        out.write(ByteBuffer.allocate(4).putInt(id).array())
    }

    private fun getSchemaId(
        subject: String?,
        schema: Schema?
    ): Int {
        return if (autoRegisterSchema) {
            registerWithRetry(subject, schema)
        } else {
            getSchemaIdWithRetry(subject, schema)
        }
    }
}
