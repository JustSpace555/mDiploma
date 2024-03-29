package data

import data.model.Transaction
import data.transaction.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class DagTangleRepository(private val repository: TransactionRepository) {
    private val tips = mutableSetOf<Transaction>()
    private val mutex = Mutex()
    private val _approvedTransactionsFlow = MutableSharedFlow<String>()
    private val _newTransactionsFlow = MutableSharedFlow<Transaction>()

    suspend fun insertNewTransaction(newTransaction: Transaction) {
        mutex.withLock {
            tips.add(newTransaction)
            _newTransactionsFlow.emit(newTransaction)
            newTransaction.prevIds.forEach { prevId ->
                if (tips.count { tip -> tip.prevIds.contains(prevId) } >= 2) {
                    tips.find { tip -> tip.signedHash == prevId }?.let { transaction ->
                        tips.remove(transaction)
                        repository.insert(transaction)
                        _approvedTransactionsFlow.emit(transaction.signedHash)
                    }
                }
            }
        }
    }

    suspend fun getTips(): Pair<String, String> = mutex.withLock {
        val first = tips.random()
        first.signedHash to (tips - first).random().signedHash
    }

    fun subscribeToApprovedTransactionUpdates(): Flow<String> = _approvedTransactionsFlow

    fun subscribeToNewTransactionUpdates(): Flow<Transaction> = _newTransactionsFlow
}