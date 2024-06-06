package domain.usecase

import data.DagTangleRepository
import io.ktor.server.sse.*

class SubscribeToNewTransactionUpdatesUseCase(private val repository: DagTangleRepository) {

    context(ServerSSESession)
    suspend operator fun invoke() {
        repository.subscribeToNewTransactionUpdates().collect { jsonString -> send(data = jsonString) }
    }
}