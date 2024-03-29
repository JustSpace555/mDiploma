package domain.usecase

import data.DagTangleRepository
import io.ktor.server.application.*
import io.ktor.util.pipeline.*
import utils.respondSse

class SubscribeToApprovedTransactionUpdatedUseCase(private val repository: DagTangleRepository) {

    context(PipelineContext<Unit, ApplicationCall>)
    suspend operator fun invoke() {
        call.respondSse(repository.subscribeToApprovedTransactionUpdates())
    }
}