package micro.apps.kstream.serializer

import com.github.thake.kafka.avro4k.serializer.AbstractKafkaAvro4kSerDeConfig
import org.apache.kafka.common.config.ConfigDef

abstract class AbstractCryptoKafkaAvro4kSerDeConfig(configDef: ConfigDef, props: Map<String, Any?>) :
    AbstractKafkaAvro4kSerDeConfig(
        configDef.define(
            CRYPTO_KEY_FILE_CONFIG,
            ConfigDef.Type.STRING,
            null,
            ConfigDef.Importance.LOW,
            CRYPTO_KEY_FILE_DOC
        ).define(
            CRYPTO_ASSOCIATED_DATA_FIELD_CONFIG,
            ConfigDef.Type.STRING,
            null,
            ConfigDef.Importance.LOW,
            CRYPTO_ASSOCIATED_DATA_FIELD_DOC
        ).define(
            CRYPTO_USE_KMS_CONFIG,
            ConfigDef.Type.BOOLEAN,
            false,
            ConfigDef.Importance.LOW,
            CRYPTO_USE_KMS_DOC
        ).define(
            CRYPTO_KMS_URI_CONFIG,
            ConfigDef.Type.STRING,
            null,
            ConfigDef.Importance.LOW,
            CRYPTO_KMS_URI_DOC
        ).define(
            CRYPTO_CRED_FILE_CONFIG,
            ConfigDef.Type.STRING,
            null,
            ConfigDef.Importance.LOW,
            CRYPTO_CRED_FILE_DOC
        ).define(
            CRYPTO_IGNORE_ERRORS_CONFIG,
            ConfigDef.Type.BOOLEAN,
            false,
            ConfigDef.Importance.LOW,
            CRYPTO_IGNORE_ERRORS_DOC
        ),
        props
    ) {
    companion object {
        const val CRYPTO_ASSOCIATED_DATA_FIELD_CONFIG = "crypto.associatedData"
        const val CRYPTO_ASSOCIATED_DATA_FIELD_DOC = "record's field name to used for associated data, should not be encrypted"
        const val CRYPTO_KEY_FILE_CONFIG = "crypto.keyFile"
        const val CRYPTO_KEY_FILE_DOC = "keySet file path generated with tinkey tool."
        const val CRYPTO_USE_KMS_CONFIG = "crypto.useKms"
        const val CRYPTO_USE_KMS_DOC = "If the keySet file is encrypted with KMS, set this flag to true."
        const val CRYPTO_KMS_URI_CONFIG = "crypto.kmsUri"
        const val CRYPTO_KMS_URI_DOC = "KMS KeyRing URI (optional)."
        const val CRYPTO_CRED_FILE_CONFIG = "crypto.credentialFile"
        const val CRYPTO_CRED_FILE_DOC = "GEK or AWS credentials JSON file (optional)."
        const val CRYPTO_IGNORE_ERRORS_CONFIG = "crypto.ignoreDecryptFailures"
        const val CRYPTO_IGNORE_ERRORS_DOC = "Ignore decrypt failures, in case the payload is not encrypted (optional)"
    }

    val keyFile: String?
        get() = this.get(CRYPTO_KEY_FILE_CONFIG) as String?
    val associatedDataField: String?
        get() = this.get(CRYPTO_ASSOCIATED_DATA_FIELD_CONFIG) as String?
    val useKms: Boolean
        get() = this.get(CRYPTO_USE_KMS_CONFIG) as Boolean
    val kmsUri: String?
        get() = this.get(CRYPTO_KMS_URI_CONFIG) as String?
    val credentialFile: String?
        get() = this.get(CRYPTO_CRED_FILE_CONFIG) as String?
    val ignoreDecryptFailures: Boolean
        get() = this.get(CRYPTO_IGNORE_ERRORS_CONFIG) as Boolean
}
