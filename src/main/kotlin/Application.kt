import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.slf4j.LoggerFactory
import data.Database
import data.dataModule
import domain.domainModule
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin
import routing.routingModule
import java.security.KeyPairGenerator
import java.security.MessageDigest

fun main() {
//    val keyStoreFile = File("build/keystore.jks")
//    val keyStore = buildKeyStore {
//        certificate(SSL_CERT_ALIAS) {
//            password = SSL_PRIVATE_KEY_PASS
//            domains = listOf("localhost")
//        }
//    }.apply {
//        saveToFile(keyStoreFile, SSL_KEY_STORE_PASS)
//    }

    val environment = applicationEngineEnvironment {
        log = LoggerFactory.getLogger("ktor.application")
        connector {
            port = 8080
        }
//        sslConnector(
//            keyStore = keyStore,
//            keyAlias = SSL_CERT_ALIAS,
//            keyStorePassword = SSL_KEY_STORE_PASS::toCharArray,
//            privateKeyPassword = SSL_PRIVATE_KEY_PASS::toCharArray,
//            builder = {
//                port = 8443
//                keyStorePath = keyStoreFile
//            }
//        )
        module {
            install(Koin) {
                modules(
                    dataModule,
                    domainModule,
                    routingModule,
                )
            }

            val database by inject<Database>()
            database.init()
            Signature
            install(ContentNegotiation) {
                json()
            }
//            install(WebSockets) {
//                pingPeriod = Duration.ofSeconds(15)
//                timeout = Duration.ofSeconds(15)
//                maxFrameSize = Long.MAX_VALUE
//                masking = false
//            }
            configureMonitoring()
            routing {
                get("/") {
                    call.respondText("Hello World!")
                }

                sensorRouting()
            }
        }
    }
    embeddedServer(Netty, environment).start(wait = true)
}

private const val SSL_CERT_ALIAS = "diploma"
private const val SSL_KEY_STORE_PASS = "diploma"
private const val SSL_PRIVATE_KEY_PASS = "diploma"

/*
fun main(args: Array<String>) {
    val generator = KeyPairGenerator.getInstance("Ed25519")
    val keyPair = generator.generateKeyPair()

    val msg = "test".toByteArray(Charsets.UTF_8)
    val digest = MessageDigest.getInstance("SHA-256")
    val msg256 = digest.digest(msg)
    println(msg.toString())
    println(msg256)

    val sig = java.security.Signature.getInstance("Ed25519")
    sig.initSign(keyPair.private)
    sig.update(msg256)
    val signed = sig.sign()
    println(java.util.Base64.getEncoder().encodeToString(signed))


    sig.initVerify(keyPair.public)
    sig.update(msg256)
    println(sig.verify(signed))

    println(java.util.Base64.getEncoder().encodeToString(keyPair.public.encoded).length)
}

 */