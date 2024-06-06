package data

import data.model.Transaction
import data.sensor.SensorRepository
import data.transaction.TransactionRepository
import domain.SensorSignature
import domain.model.TransactionDto
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import utils.getNotNullOrRespondError

class DagTangleRepository(
    private val transactionRepository: TransactionRepository,
    private val sensorRepository: SensorRepository,
    private val signature: SensorSignature,
) {
    private val tips = mutableSetOf<Transaction>()
    private val tipsMap = mutableMapOf<String, Int>()
    private val approvedTips = mutableListOf<Transaction>()
    private val mutex = Mutex()
    private val _approvedTransactionsFlow = MutableSharedFlow<Transaction>()
    private val _newTransactionsFlow = MutableSharedFlow<Transaction>()

    context(RoutingContext)
    suspend fun insertNewTransaction(newTransaction: Transaction) {
        mutex.withLock {
            tips.add(newTransaction)
            tipsMap[newTransaction.signedHash] = 0
            _newTransactionsFlow.emit(newTransaction)

            newTransaction.prevIds.forEach { prevSignedHash ->
                val prevTip = getNotNullOrRespondError(
                    tips.find { tip -> tip.signedHash == prevSignedHash },
                    "Missing tip with hash: $prevSignedHash"
                ) ?: return
                val publicKeyStr = getNotNullOrRespondError(
                    sensorRepository.get(prevTip.sensorId),
                    "Missing publicKey for sensor with this id"
                )?.publicKey ?: return
                val publicKey = signature.publicKeyFromString(publicKeyStr)
                val isVerified = signature.checkMessageSignature(
                    expectedUnsignedHash = signature.hashMessage(prevTip.toSignatureFormat()),
                    actualSignedHash = prevTip.signedHash.toByteArray(Charsets.UTF_8),
                    publicKey = publicKey
                )

                if (!isVerified) {
                    call.respond(HttpStatusCode.Forbidden, "Signature check failed")
                    return
                }
                tipsMap[prevSignedHash]?.let { amountPointers ->
                    val newPointersValue = amountPointers + 1
                    tipsMap[prevSignedHash] = newPointersValue
                    if (newPointersValue >= 2) approvedTips.add(prevTip)
                }
            }

            approvedTips.forEach { approvedTransaction ->
                tips.remove(approvedTransaction)
                tipsMap.remove(approvedTransaction.signedHash)
                transactionRepository.insert(approvedTransaction)
                _approvedTransactionsFlow.emit(approvedTransaction)
            }
            approvedTips.clear()
        }
    }

    suspend fun getTips(): String = mutex.withLock {
        when (tips.size) {
            0 -> ""
            1 -> tips.first().signedHash
            else -> {
                val first = tips.random()
                "${first.signedHash},${(tips - first).random().signedHash}"
            }
        }
    }

    fun subscribeToApprovedTransactionUpdates(): Flow<String> =
        _approvedTransactionsFlow.map(::encodeTransactionToJsonString)

    fun subscribeToNewTransactionUpdates(): Flow<String> = _newTransactionsFlow.map(::encodeTransactionToJsonString)

    private fun Transaction.toSignatureFormat() =
        "$sensorId,${prevIds.joinToString(separator = ",")},$gasLevel,$epochTime"

    private fun encodeTransactionToJsonString(transaction: Transaction) = Json.encodeToString(
        TransactionDto(
            sensorId = transaction.sensorId,
            prevIds = transaction.prevIds,
            gasLevel = transaction.gasLevel,
            epochTime = transaction.epochTime,
            signedHash = transaction.signedHash,
        )
    )
}