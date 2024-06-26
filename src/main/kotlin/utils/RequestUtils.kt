package utils

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

suspend fun <T> RoutingContext.getOrRespondError(
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

suspend fun <T> RoutingContext.getNotNullOrRespondError(
    value: T?,
    errorMessage: String,
): T? = value ?: run {
    call.respond(HttpStatusCode.BadRequest, errorMessage)
    null
}

suspend fun RoutingContext.checkOrRespondError(
    errorMessage: String,
    errorCode: HttpStatusCode = HttpStatusCode.BadRequest,
    check: () -> Boolean,
): Boolean {
    val checkResult = check()
    if (!checkResult) call.respond(errorCode, errorMessage)
    return checkResult
}