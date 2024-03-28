package data.sensor

import org.jetbrains.exposed.sql.Table

object SensorTable : Table() {
    val id = integer("id")
    val vin = varchar("vin", 17)
    val gasLiters = integer("gasLiters")
    val publicKey = varchar("publicKey", 60)

    override val primaryKey = PrimaryKey(id)
}