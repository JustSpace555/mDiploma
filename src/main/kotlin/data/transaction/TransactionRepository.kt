package data.transaction

import data.model.Transaction
import org.jetbrains.exposed.sql.insert
import utils.dbQuery

class TransactionRepository(private val table: TransactionTable) {

    suspend fun insert(transaction: Transaction) = dbQuery {
        table.insert {
            it[signature] = transaction.signedHash
            it[prevIds] = transaction.prevIds
            it[sensorId] = transaction.sensorId
            it[gasLevel] = transaction.gasLevel.toDouble()
            it[epochTime] = transaction.epochTime
        }
    }
}