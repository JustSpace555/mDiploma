package data.sensor

import org.jetbrains.exposed.sql.Table

object SensorTable : Table() {
    val id = integer("id")
    val vin = varchar("vin", 17)
    val gasLiters = float("gasLiters")
    val publicKey = array<Byte>("publicKey", 32)

    override val primaryKey = PrimaryKey(id)
}