package routing

import io.ktor.server.routing.*
import domain.usecase.RegisterNewSensorByOperatorUseCase
import domain.usecase.RegisterSensorPublicKeyUseCase

class SensorRouting(
    private val registerNewSensorByOperatorUseCase: RegisterNewSensorByOperatorUseCase,
    private val registerSensorPublicKeyUseCase: RegisterSensorPublicKeyUseCase,
) {

    fun Routing.sensorRouting() {
        post("/operator/register/sensor") {
            registerNewSensorByOperatorUseCase()
        }

        post("/sensor/register/{id}") {
            registerSensorPublicKeyUseCase()
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