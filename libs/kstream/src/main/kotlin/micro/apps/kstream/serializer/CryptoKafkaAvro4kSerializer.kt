package micro.apps.kstream.serializer

import com.github.thake.kafka.avro4k.serializer.AbstractKafkaAvro4kSerializer
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient
import org.apache.kafka.common.serialization.Serializer

class CryptoKafkaAvro4kSerializer(
    client: SchemaRegistryClient? = null,
    props: Map<String, *>? = null
) : AbstractKafkaAvro4kSerializer(), Serializer<Any?> {
    override fun serialize(topic: String?, data: Any?): ByteArray {
        TODO("Not yet implemented")
    }
}
