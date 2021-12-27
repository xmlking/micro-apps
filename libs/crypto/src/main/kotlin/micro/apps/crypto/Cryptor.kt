package micro.apps.crypto

import com.google.crypto.tink.Aead
import com.google.crypto.tink.CleartextKeysetHandle
import com.google.crypto.tink.JsonKeysetReader
import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.aead.KmsAeadKeyManager
import com.google.crypto.tink.integration.gcpkms.GcpKmsClient
import micro.apps.core.toOptional
import mu.KotlinLogging
import java.io.File
import java.io.IOException
import java.security.GeneralSecurityException

interface Cryptor {
    var aead: Aead // abstract property

    /**
     * Encrypts `plaintext` with `associatedData` as associated authenticated data.
     * The resulting ciphertext allows for checking authenticity and integrity of associated data
     * (`associatedData`), but does not guarantee its secrecy.
     *
     * @param plaintext       the plaintext to be encrypted. It must be non-null, but can also
     * be an empty (zero-length) byte array
     * @param associatedData  associated data to be authenticated, but not encrypted.  Associated data
     * is optional, so this parameter can be null.  In this case the null value
     * is equivalent to an empty (zero-length) byte array.
     * For successful decryption the same associatedData must be provided
     * along with the ciphertext.
     * @return resulting ciphertext
     */
    @Throws(GeneralSecurityException::class)
    fun encrypt(plaintext: ByteArray, associatedData: ByteArray? = null): ByteArray =
        aead.encrypt(plaintext, associatedData)

    /**
     * Decrypts `ciphertext` with `associatedData` as associated authenticated data.
     * The decryption verifies the authenticity and integrity of the associated data, but there are
     * no guarantees wrt. secrecy of that data.
     *
     * @param ciphertext      the plaintext to be decrypted. It must be non-null.
     * @param associatedData  associated data to be authenticated.  For successful decryption
     * it must be the same as associatedData used during encryption.
     * Can be null, which is equivalent to an empty (zero-length) byte array.
     * @return resulting plaintext
     */
    @Throws(GeneralSecurityException::class)
    fun decrypt(ciphertext: ByteArray, associatedData: ByteArray? = null): ByteArray =
        aead.decrypt(ciphertext, associatedData)
}

private val logger = KotlinLogging.logger {}

/**
 * Return {@code Cryptor} implementation from encrypted {@code keyFile} if {@code useKms} is enabled,
 * Creates and registers a {@link #GcpKmsClient} with the Tink runtime,
 * Otherwise expects unencrypted {@code keyFile}
 *
 * <p>If {@code kekUri} is present, it is the only key that the new client will support. Otherwise
 * the new client supports all GCP KMS keys.
 *
 * <p>If {@code credentialPath} is present, load the credentials from that. Otherwise use the
 * default credentials.
 */
class CryptorImpl @Throws(IOException::class, GeneralSecurityException::class) constructor(
    keyFile: String,
    useKms: Boolean = false,
    kekUri: String? = null,
    credentialFile: String? = null
) : Cryptor {
    override lateinit var aead: Aead

    init {
        // Register all AEAD key types with to Tink runtime.
        AeadConfig.register()

        val handle: KeysetHandle = if (useKms) {
            logger.info("Creating GcpKmsClient with kekUri={}, credentialFile={}", kekUri, credentialFile)

            // initialize GcpKmsClient
            // TODO: if kekUri starts with `gcp-kms` initialize GcpKmsClient, for `aws-kms`
            try {
                GcpKmsClient.register(kekUri.toOptional(), credentialFile.toOptional())
            } catch (ex: GeneralSecurityException) {
                logger.error("Error initializing GcpKmsClient", ex)
                throw ex
            }

            // Get the kekAead
            val kekAead: Aead = try {
                val handle = KeysetHandle.generateNew(KmsAeadKeyManager.createKeyTemplate(kekUri))
                handle.getPrimitive(Aead::class.java)
            } catch (ex: GeneralSecurityException) {
                logger.error("Error initializing masterKey KeysetHandle", ex)
                throw ex
            }

            // Get the KeysetHandle from encrypted keyFile with KMS
            try {
                KeysetHandle.read(JsonKeysetReader.withFile(File(keyFile)), kekAead)
            } catch (ex: GeneralSecurityException) {
                logger.error("Error initializing encrypted KeysetHandle", ex)
                throw ex
            } catch (ex: IOException) {
                logger.error("Error loading encrypted KeysetHandle", ex)
                throw ex
            }
        } else {
            // Get the KeysetHandle from unencrypted keyFile
            try {
                CleartextKeysetHandle.read(JsonKeysetReader.withFile(File(keyFile)))
            } catch (ex: GeneralSecurityException) {
                logger.error("Error initializing unencrypted KeysetHandle", ex)
                throw ex
            } catch (ex: IOException) {
                logger.error("Error loading unencrypted KeysetHandle", ex)
                throw ex
            }
        }

        // Get the primitive.
        aead = try {
            handle.getPrimitive(Aead::class.java)
        } catch (ex: GeneralSecurityException) {
            logger.error("Cannot create primitive", ex)
            throw ex
        }
    }
}
