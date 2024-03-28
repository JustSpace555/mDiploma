package domain.usecase

import data.DagTangleRepository
import data.model.Transaction
import data.sensor.SensorRepository
import domain.SensorSignature
import domain.model.TransactionDto
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*
import utils.checkOrRespondError
import utils.getNotNullOrRespondError
import utils.getOrRespondError

class RegisterNewTransactionUseCase(
    private val dagTangleRepository: DagTangleRepository,
    private val sensorRepository: SensorRepository,
    private val signature: SensorSignature,
) {

    context(PipelineContext<Unit, ApplicationCall>)
    suspend operator fun invoke() {
        val body = getOrRespondError(errorMessage = "Missing request body") { call.receive<TransactionDto>() } ?: return

        val transaction = Transaction(
            sensorId = getNotNullOrRespondError(body.sensorId, "Missing sensorId value") ?: return,
            prevIds = run {
                val prevIds = getNotNullOrRespondError(body.oldTransactions, "Missing prevIds value") ?: return
                prevIds.takeIf { list ->
                    checkOrRespondError("Amount of previous transaction must be at least 2") { list.size >= 2 }
                } ?: return
            },
            epochTime = getNotNullOrRespondError(body.timeStamp, "Missing timeStamp value") ?: return,
            gasLevel = run {
                val value = getNotNullOrRespondError(body.gasLevel, "Missing gasLevel value") ?: return
                value.takeIf {
                    checkOrRespondError("gasLevel must be in double format") { value.toDoubleOrNull() != null }
                } ?: return
            },
            signedHash = getNotNullOrRespondError(body.signedHash, "Missing signedHash value") ?: return,
        )

        val publicKeyStr = getNotNullOrRespondError(
            sensorRepository.get(transaction.sensorId),
            "Missing publicKey for sensor with this id"
        )?.publicKey ?: return
        val publicKey = signature.publicKeyFromString(publicKeyStr)
        val isVerified = signature.checkMessageSignature(
            expectedUnsignedHash = signature.hashMessage(transaction.toSignatureFormat()),
            actualSignedHash = transaction.signedHash.toByteArray(Charsets.UTF_8),
            publicKey = publicKey
        )

        if (!isVerified) {
            call.respond(HttpStatusCode.Forbidden, "Signature check failed")
            return
        }

        dagTangleRepository.insertNewTransaction(transaction)
        call.respond(HttpStatusCode.OK)
    }

    private fun Transaction.toSignatureFormat() =
        "$sensorId,${prevIds.joinToString(separator = ",")},$gasLevel,$epochTime"
}