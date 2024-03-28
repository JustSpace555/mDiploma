package domain.usecase

import data.DagTangleRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*

class RequestTipsUseCase(private val repository: DagTangleRepository) {

    context(PipelineContext<Unit, ApplicationCall>)
    suspend operator fun invoke() {
        val (first, second) = repository.getTips()
        call.respond(HttpStatusCode.OK, "$first,$second")
    }
}