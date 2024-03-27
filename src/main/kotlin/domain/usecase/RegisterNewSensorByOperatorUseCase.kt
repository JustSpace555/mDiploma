package domain.usecase

import data.sensor.SensorRepository
import domain.SensorDto
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*

class RegisterNewSensorByOperatorUseCase(private val repository: SensorRepository) {

    suspend operator fun PipelineContext<Unit, ApplicationCall>.invoke() {
        val dto = try {
            call.receive<SensorDto>()
        } catch (e: ContentTransformationException) {
            call.respond(HttpStatusCode.ExpectationFailed)
        }
        val sensorDto = call.receive<SensorDto>()
        val sensor = try {
            SensorMapper.map(sensorDto)
        } catch (e: IllegalArgumentException) {
            call.respond(HttpStatusCode.BadRequest, e.localizedMessage)
            return@post
        }
        sensorSchema.insert(sensor)
        call.respond(HttpStatusCode.OK)
    }
}