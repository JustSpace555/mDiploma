package domain.usecase

import data.DagTangleRepository
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

class RequestTipsUseCase(private val repository: DagTangleRepository) {

    context(RoutingContext)
    suspend operator fun invoke() = call.respond(HttpStatusCode.OK, repository.getTips())
}