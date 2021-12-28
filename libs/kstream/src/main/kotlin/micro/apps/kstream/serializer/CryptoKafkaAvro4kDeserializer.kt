package micro.apps.kstream.serializer

import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient
import micro.apps.core.getThroughReflection
import org.apache.avro.Schema
import org.apache.kafka.common.serialization.Deserializer

class CryptoKafkaAvro4kDeserializer(
    client: SchemaRegistryClient? = null,
    props: Map<String, *>? = null
) : AbstractCryptoKafkaAvro4kDeserializer(), Deserializer<Any?> {
    private var isKey = false
    private var associatedDataField: String? = null

    init {
        props?.let { configure(this.deserializerConfig(it)) }
        // Set the registry client explicitly after the configuration has been applied to override client from configuration
        if (client != null) this.schemaRegistry = client
    }

    override fun configure(configs: Map<String, *>, isKey: Boolean) {
        this.isKey = isKey
        this.associatedDataField = configs[AbstractCryptoKafkaAvro4kSerDeConfig.CRYPTO_ASSOCIATED_DATA_FIELD_CONFIG] as String?
        this.configure(CryptoKafkaAvro4kDeserializerConfig(configs))
    }


    override fun deserialize(s: String?, bytes: ByteArray?): Any? {
        // TODO: how to get associatedData?
        return this.deserialize(s, this.decrypt(bytes!!), null)
    }

    fun deserialize(@Suppress("UNUSED_PARAMETER") topic: String?, data: ByteArray?, readerSchema: Schema?): Any? {
        return this.deserialize(data, readerSchema)
    }

    override fun close() {}
}
