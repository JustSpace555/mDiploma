package data.sensor

import domain.SensorDto
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update
import utils.dbQuery

class SensorRepository(private val table: SensorTable) {

    suspend fun insert(sensor: Sensor) = dbQuery {
        table.insert {
            it[id] = sensor.id
            it[vin] = sensor.vin
            it[gasLiters] = sensor.gasLiters
            it[publicKey] = sensor.publicKey
        }
    }

    suspend fun get(id: Int) = dbQuery {
        table.select { table.id eq id }
            .map {
                SensorDto(
                    id = id,
                    vin = it[table.vin],
                    gasLiters = it[table.gasLiters],
                    publicKey = it[table.publicKey],
                )
            }.singleOrNull()
    }

    suspend fun delete(id: Int) = dbQuery { table.deleteWhere { table.id eq id } }

    suspend fun update(id: Int, vin: String? = null, gasLiters: Int? = null, publicKey: String? = null) = dbQuery {
        table.update({ table.id eq id }) { statement ->
            vin?.let { vinNotNull -> statement[table.vin] = vinNotNull }
            gasLiters?.let { litersNotNull -> statement[table.gasLiters] = litersNotNull }
            publicKey?.let { keyNotNull -> statement[table.publicKey] = keyNotNull }
        }
    }
}