package data.transaction

import org.jetbrains.exposed.sql.Table

object TransactionTable : Table() {
    val pow = varchar("pow", 64)
    val prevId1 = varchar("prevId1", 64)
    val prevId2 = varchar("prevId2", 64)
    val vin = varchar("vin", 17)
    val gasLevel = double("gasLevel")
    val signature = varchar("signature", 64)
}