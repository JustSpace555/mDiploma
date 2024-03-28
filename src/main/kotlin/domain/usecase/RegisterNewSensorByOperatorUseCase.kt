package domain.usecase

import data.model.Sensor
import data.sensor.SensorRepository
import domain.model.SensorDto
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*
import utils.getNotNullOrRespondError
import utils.getOrRespondError

class RegisterNewSensorByOperatorUseCase(private val repository: SensorRepository) {

    context(PipelineContext<Unit, ApplicationCall>)
    suspend operator fun invoke() {
        val dto = getOrRespondError(errorMessage = "Missing sensor info") {
            call.receive<SensorDto>()
        } ?: return

        val sensor = Sensor(
            id = getNotNullOrRespondError(dto.id, "Missing id field") ?: return,
            vin = getNotNullOrRespondError(dto.vin, "Missing vin field") ?: return,
            gasLiters = getNotNullOrRespondError(dto.gasLiters, "Missing gasLiters field") ?: return,
            publicKey = "" // Will be inserted with sensor registration
        )

        repository.insert(sensor)
        call.respond(HttpStatusCode.OK)
    }
}