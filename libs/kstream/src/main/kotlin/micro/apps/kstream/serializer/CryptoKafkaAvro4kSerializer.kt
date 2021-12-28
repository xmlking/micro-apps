package micro.apps.kstream.serializer

import io.confluent.kafka.schemaregistry.avro.AvroSchema
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient
import micro.apps.core.getThroughReflection
import mu.KotlinLogging
import org.apache.kafka.common.serialization.Serializer

class CryptoKafkaAvro4kSerializer(
    client: SchemaRegistryClient? = null,
    props: Map<String, *>? = null
) : AbstractCryptoKafkaAvro4kSerializer(), Serializer<Any?> {
    private var isKey = false
    private var associatedDataField: String? = null

    init {
        props?.let { configure(this.serializerConfig(it)) }
        // Set the registry client explicitly after configuration has been applied to override client from configuration
        if (client != null) this.schemaRegistry = client
    }

    override fun configure(configs: Map<String, *>, isKey: Boolean) {
        this.isKey = isKey
        this.associatedDataField = configs[AbstractCryptoKafkaAvro4kSerDeConfig.CRYPTO_ASSOCIATED_DATA_FIELD_CONFIG] as String?
        this.configure(CryptoKafkaAvro4kSerializerConfig(configs))
    }

    override fun serialize(topic: String?, record: Any?): ByteArray? {
        return record?.let {
            /* TODO: how to get associatedData at Deserializer side???
            val associatedData = associatedDataField?.let {
                record.getThroughReflection<String>(this.associatedDataField!!)
            }?.toByteArray()
             */
            val aaa = this.serializeImpl(
                this.getSubjectName(
                    topic,
                    isKey,
                    it,
                    AvroSchema(avroSchemaUtils.getSchema(it))
                ), it
            )
            // TODO: add associatedData, will reflect expensive?
            this.encrypt(aaa!!)
        }
    }

    override fun close() {}
}
