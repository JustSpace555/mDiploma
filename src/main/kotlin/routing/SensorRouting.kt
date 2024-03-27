package routing

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import domain.SensorDto

fun Routing.sensorRouting() {
    post("/operator/register/sensor") {
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

    post("/sensor/register/{id}") {
        val id = call.parameters["id"]?.toInt() ?: run {
            call.respond(HttpStatusCode.BadRequest, "Id is missing")
            return@post
        }

    }

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
}