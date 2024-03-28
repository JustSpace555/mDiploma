package utils

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*

suspend fun <T> PipelineContext<Unit, ApplicationCall>.getOrRespondError(
    errorCode: HttpStatusCode = HttpStatusCode.BadRequest,
    errorMessage: String? = null,
    block: suspend () -> T?
): T? = try {
    block()
} catch (e : Throwable) {
    errorMessage?.let { message ->
        call.respond(errorCode, message)
    } ?: call.respond(errorCode)
    null
}

suspend fun <T> PipelineContext<Unit, ApplicationCall>.getNotNullOrRespondError(
    value: T?,
    errorMessage: String,
): T? = value ?: run {
    call.respond(HttpStatusCode.BadRequest, errorMessage)
    null
}