package domain.usecase

import data.DagTangleRepository
import io.ktor.server.sse.*

class SubscribeToApprovedTransactionUpdatedUseCase(private val repository: DagTangleRepository) {

    context(ServerSSESession)
    suspend operator fun invoke() {
        repository.subscribeToApprovedTransactionUpdates().collect { string -> send(data = string) }
    }
}