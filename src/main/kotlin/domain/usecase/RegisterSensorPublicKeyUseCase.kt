package domain.usecase

import data.sensor.SensorRepository
import domain.model.RegisterSensorPublicKeyDto
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.utils.io.*
import kotlinx.serialization.json.Json
import utils.getNotNullOrRespondError
import java.nio.charset.Charset

class RegisterSensorPublicKeyUseCase(private val repository: SensorRepository) {

    context(RoutingContext)
    suspend operator fun invoke() {
        val sensorId = getNotNullOrRespondError(
            call.parameters["id"]?.toInt(),
            "Missing id param"
        ) ?: return

        val channel = call.request.receiveChannel()
        val ba = ByteArray(channel.availableForRead)
        channel.readFully(ba)
        val bodyStr = ba.toString(Charset.defaultCharset())
        val body = Json.decodeFromString<RegisterSensorPublicKeyDto>(bodyStr)
        val publicKey = getNotNullOrRespondError(body.publicKey, "Missing publicKey value") ?: return

        repository.update(id = sensorId, publicKey = publicKey.map { it.toByte() }.toByteArray())
        call.respond(HttpStatusCode.OK, repository.get(sensorId)!!.gasLiters)
    }
}