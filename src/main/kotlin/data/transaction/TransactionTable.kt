package data.transaction

import data.sensor.SensorTable
import org.jetbrains.exposed.sql.Table

object TransactionTable : Table() {
    val signature = varchar("signature", 64)
    val sensorId = integer("sensorId") references SensorTable.id
    val prevIds = array<String>("prevIds")
    val gasLevel = double("gasLevel")
    val epochTime = long("epochTime")

    override val primaryKey = PrimaryKey(signature)
}