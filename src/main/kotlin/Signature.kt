import net.i2p.crypto.eddsa.EdDSAPrivateKey
import net.i2p.crypto.eddsa.EdDSAPublicKey
import net.i2p.crypto.eddsa.EdDSASecurityProvider
import net.i2p.crypto.eddsa.KeyPairGenerator
import java.security.SecureRandom
import java.security.Security

object Signature {
    val privateKey: EdDSAPrivateKey
    val publicKey: EdDSAPublicKey

    init {
        Security.addProvider(EdDSASecurityProvider())
        val keyPair = KeyPairGenerator().apply {
            initialize(256, SecureRandom())
        }.generateKeyPair()

        privateKey = keyPair.private as EdDSAPrivateKey
        publicKey = keyPair.public as EdDSAPublicKey
    }

    private val engineDecrypt by lazy {
//        EdDSAEngine().verifyOneShot()
    }
}