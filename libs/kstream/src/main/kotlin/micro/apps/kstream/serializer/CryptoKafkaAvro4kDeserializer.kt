package micro.apps.kstream.serializer

import com.github.thake.kafka.avro4k.serializer.AbstractKafkaAvro4kDeserializer
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient
import org.apache.kafka.common.serialization.Deserializer

class CryptoKafkaAvro4kDeserializer(
    client: SchemaRegistryClient? = null,
    props: Map<String, *>? = null
) : AbstractKafkaAvro4kDeserializer(), Deserializer<Any?> {
    override fun deserialize(topic: String?, data: ByteArray?): Any? {
        TODO("Not yet implemented")
    }
}
