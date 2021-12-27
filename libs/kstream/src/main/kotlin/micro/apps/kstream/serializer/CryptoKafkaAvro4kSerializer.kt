package micro.apps.kstream.serializer

import io.confluent.kafka.schemaregistry.avro.AvroSchema
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient
import org.apache.kafka.common.serialization.Serializer

class CryptoKafkaAvro4kSerializer(
    client: SchemaRegistryClient? = null,
    props: Map<String, *>? = null
) : AbstractCryptoKafkaAvro4kSerializer(), Serializer<Any?> {
    private var isKey = false

    init {
        props?.let { configure(this.serializerConfig(it)) }
        // Set the registry client explicitly after configuration has been applied to override client from configuration
        if (client != null) this.schemaRegistry = client
    }

    override fun configure(configs: Map<String, *>, isKey: Boolean) {
        this.isKey = isKey
        this.configure(CryptoKafkaAvro4kSerializerConfig(configs))
    }


    override fun serialize(topic: String?, record: Any?): ByteArray? {
        return record?.let {
            val aaa = this.serializeImpl(
                this.getSubjectName(
                    topic,
                    isKey,
                    it,
                    AvroSchema(avroSchemaUtils.getSchema(it))
                ), it
            )
            this.encrypt(aaa!!)
        }
    }

    override fun close() {}
}
