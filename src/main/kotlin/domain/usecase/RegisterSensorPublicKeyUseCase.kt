package domain.usecase

import data.sensor.SensorRepository
import domain.model.SensorPublicKeyRegisterDto
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*
import utils.getNotNullOrRespondError
import utils.getOrRespondError

class RegisterSensorPublicKeyUseCase(private val repository: SensorRepository) {

    context(PipelineContext<Unit, ApplicationCall>)
    suspend operator fun invoke() {
        val sensorId = getNotNullOrRespondError(call.parameters["id"]?.toInt(), "Missing id param") ?: return
        val body = getOrRespondError(errorMessage = "Missing request body") {
            call.receive<SensorPublicKeyRegisterDto>()
        } ?: return
        val publicKey = getNotNullOrRespondError(body.publicKey, "Missing publicKey value")

        repository.update(id = sensorId, publicKey = publicKey)
        call.respond(HttpStatusCode.OK)
    }
}