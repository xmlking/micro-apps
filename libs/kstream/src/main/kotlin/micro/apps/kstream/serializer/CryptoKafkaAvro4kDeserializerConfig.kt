package micro.apps.kstream.serializer

import org.apache.kafka.common.config.ConfigDef

class CryptoKafkaAvro4kDeserializerConfig(props: Map<String, *>) : AbstractCryptoKafkaAvro4kSerDeConfig(
    baseConfigDef().define(
        RECORD_PACKAGES,
        ConfigDef.Type.STRING,
        null,
        ConfigDef.Importance.HIGH,
        "The packages in which record types annotated with @AvroName, @AvroAlias and @AvroNamespace can be found. Packages are separated by a colon ','."
    ),
    props
) {

    fun getRecordPackages() = getString(RECORD_PACKAGES)?.split(",")?.map { it.trim() }?.toList() ?: emptyList()

    companion object {
        const val RECORD_PACKAGES = "record.packages"
    }
}
