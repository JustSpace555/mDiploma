package domain

import java.security.KeyFactory
import java.security.MessageDigest
import java.security.PublicKey
import java.security.Signature
import java.security.spec.X509EncodedKeySpec

class SensorSignature {
    private val keyFactory by lazy { KeyFactory.getInstance(ED25519) }
    private val signature by lazy { Signature.getInstance(ED25519) }
    private val sha256 by lazy { MessageDigest.getInstance(SHA_256) }

    fun publicKeyFromString(publicKey: ByteArray): PublicKey =
        keyFactory.generatePublic(X509EncodedKeySpec(publicKey))

    fun hashMessage(message: String): ByteArray = sha256.digest(message.toByteArray(Charsets.UTF_8))

    fun checkMessageSignature(
        expectedUnsignedHash: ByteArray,
        actualSignedHash: ByteArray,
        publicKey: PublicKey,
    ): Boolean = with(signature) {
        initVerify(publicKey)
        update(expectedUnsignedHash)
        verify(actualSignedHash)
    }

    private companion object {
        private const val ED25519 = "Ed25519"
        private const val SHA_256 = "SHA-256"
    }
}