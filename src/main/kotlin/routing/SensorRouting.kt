package routing

import io.ktor.server.routing.*
import domain.usecase.RegisterNewTransactionUseCase
import domain.usecase.RegisterSensorPublicKeyUseCase
import domain.usecase.RequestTipsUseCase

class SensorRouting(
    private val registerSensorPublicKeyUseCase: RegisterSensorPublicKeyUseCase,
    private val requestTipsUseCase: RequestTipsUseCase,
    private val registerNewTransactionUseCase: RegisterNewTransactionUseCase,
) {

    context(Routing)
    fun routing() {
        post("/sensor/register/{id}") {
            registerSensorPublicKeyUseCase()
        }

        get("/sensor/tips") {
            requestTipsUseCase()
        }

        post("/sensor/newTransaction") {
            registerNewTransactionUseCase()
        }

        /*
        get("/sensors/{id}") {
            val id = call.parameters["id"]?.toInt() ?: run {
                call.respond(HttpStatusCode.BadRequest, "Id is missing")
                return@get
            }
            sensorSchema.get(id)
                ?.let { sensor -> call.respond(HttpStatusCode.OK, sensor) }
                ?: call.respond(HttpStatusCode.NotFound)
        }

        delete("/sensors/{id}") {
            val id = call.parameters["id"]?.toInt() ?: run {
                call.respond(HttpStatusCode.BadRequest, "Id is missing")
                return@delete
            }
            sensorSchema.delete(id)
            call.respond(HttpStatusCode.OK)
        }

         */
    }
}