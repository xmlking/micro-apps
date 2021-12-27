package micro.apps.crypto

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class CryptorTest : FunSpec({
    lateinit var cryptor: Cryptor

    beforeSpec {
        cryptor = CryptorImpl("src/test/resources/aead_keyset.json")
    }

    test("Test Cryptor bootstrapped with plaintext keySet") {
        val plaintext = "hi! this is sumo".toByteArray()
        val associatedData = "test123".toByteArray()

        val ciphertext = cryptor.encrypt(plaintext, associatedData)
        val plaintext2 = cryptor.decrypt(ciphertext, associatedData)
        plaintext2 shouldBe plaintext
    }

    test("Test Cryptor encrypt/decrypt without associatedData") {
        val plaintext = "hi! this is sumo".toByteArray()

        val ciphertext = cryptor.encrypt(plaintext)
        val plaintext2 = cryptor.decrypt(ciphertext)
        plaintext2 shouldBe plaintext
    }

    test("Test Cryptor bootstrapped with encrypted keySet").config(enabled = false) {
        cryptor = CryptorImpl("src/test/resources/aead_keyset.json", true)
        val plaintext = "hi! this is sumo".toByteArray()
        val associatedData = "test123".toByteArray()

        val ciphertext = cryptor.encrypt(plaintext)
        val plaintext2 = cryptor.decrypt(ciphertext)
        plaintext2 shouldBe plaintext
    }

    test("Test Cryptor bootstrapped with encrypted keySet and kekUri").config(enabled = false) {
        cryptor = CryptorImpl(
            "src/test/resources/aead_keyset.json",
            true,
            "gcp-kms://projects/*/locations/*/keyRings/*/cryptoKeys/*"
        )
        val plaintext = "hi! this is sumo".toByteArray()
        val associatedData = "test123".toByteArray()

        val ciphertext = cryptor.encrypt(plaintext)
        val plaintext2 = cryptor.decrypt(ciphertext)
        plaintext2 shouldBe plaintext
    }

})
