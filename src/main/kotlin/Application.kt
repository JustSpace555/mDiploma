import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import data.Database
import data.dataModule
import domain.domainModule
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.sse.*
import org.koin.core.context.startKoin
import org.koin.ktor.ext.inject
import routing.OperatorRouting
import routing.SensorRouting
import routing.routingModule

fun main() {
    startKoin {
        modules(dataModule, domainModule, routingModule)
    }
    embeddedServer(
        factory = Netty,
        port = 3000,
        module = Application::mainModule
    ).start(true)
}

private fun Application.mainModule() {
    configureMonitoring()

    install(ContentNegotiation) { json() }
    install(SSE)

    val database by inject<Database>()
    database.init()

    val sensorRouting by inject<SensorRouting>()
    val operatorRouting by inject<OperatorRouting>()
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        sensorRouting.routing()
        operatorRouting.routing()
    }
}