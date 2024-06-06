package domain.usecase

import data.DagTangleRepository
import data.model.Transaction
import domain.model.TransactionDto
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.utils.io.*
import kotlinx.serialization.json.Json
import utils.checkOrRespondError
import utils.getNotNullOrRespondError
import java.nio.charset.Charset

class RegisterNewTransactionUseCase(private val dagTangleRepository: DagTangleRepository) {

    context(RoutingContext)
    suspend operator fun invoke() {
        val channel = call.request.receiveChannel()
        val ba = ByteArray(channel.availableForRead)
        channel.readFully(ba)
        val bodyStr = ba.toString(Charset.defaultCharset())
        val body = Json.decodeFromString<TransactionDto>(bodyStr)

        val transaction = Transaction(
            sensorId = getNotNullOrRespondError(body.sensorId, "Missing sensorId value") ?: return,
            prevIds = body.prevIds ?: emptyList(),
            epochTime = getNotNullOrRespondError(body.epochTime, "Missing timeStamp value") ?: return,
            gasLevel = run {
                val value = getNotNullOrRespondError(body.gasLevel, "Missing gasLevel value") ?: return
                value.takeIf {
                    checkOrRespondError("gasLevel must be in Double format") {
                        value.toDoubleOrNull() != null
                    }
                } ?: return
            },
            signedHash = getNotNullOrRespondError(body.signedHash, "Missing signedHash value") ?: return,
        )

        dagTangleRepository.insertNewTransaction(transaction)
        call.respond(HttpStatusCode.OK)
    }
}