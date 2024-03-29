package domain.usecase

import data.DagTangleRepository
import domain.model.TransactionDto
import io.ktor.server.application.*
import io.ktor.util.pipeline.*
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import utils.respondSse

class SubscribeToNewTransactionUpdatesUseCase(private val repository: DagTangleRepository) {

    context(PipelineContext<Unit, ApplicationCall>)
    suspend operator fun invoke() {
        call.respondSse(
            repository.subscribeToNewTransactionUpdates().map { transaction ->
                Json.encodeToString(
                    TransactionDto(
                        sensorId = transaction.sensorId,
                        oldTransactions = transaction.prevIds,
                        gasLevel = transaction.gasLevel,
                        timeStamp = transaction.epochTime,
                        signedHash = transaction.signedHash,
                    )
                )
            }
        )
    }
}