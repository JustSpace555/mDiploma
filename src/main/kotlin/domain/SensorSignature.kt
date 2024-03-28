package domain

import java.security.KeyFactory
import java.security.MessageDigest
import java.security.PublicKey
import java.security.Signature
import java.security.spec.X509EncodedKeySpec

class SensorSignature {
    private val keyFactory = KeyFactory.getInstance(ED25519)
    private val signature = Signature.getInstance(ED25519)
    private val sha256 = MessageDigest.getInstance(SHA_256)

    fun publicKeyFromString(publicKey: String): PublicKey =
        keyFactory.generatePublic(X509EncodedKeySpec(publicKey.toByteArray(Charsets.UTF_8)))

    fun hashMessage(message: String): ByteArray = sha256.digest(message.toByteArray(Charsets.UTF_8))

    fun checkMessageSignature(
        expectedUnsignedHash: ByteArray,
        actualSignedHash: ByteArray,
        publicKey: PublicKey,
    ): Boolean {
        signature.initVerify(publicKey)
        signature.update(expectedUnsignedHash)
        return signature.verify(actualSignedHash)
    }

    private companion object {
        private const val ED25519 = "Ed25519"
        private const val SHA_256 = "SHA-256"
    }
}