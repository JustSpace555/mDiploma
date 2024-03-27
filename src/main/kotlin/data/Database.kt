package data

import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import data.sensor.SensorTable

class Database(private val sensorTable: SensorTable) {
    fun init() {
        val database = org.jetbrains.exposed.sql.Database.connect(
            url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
            user = "root",
            driver = "org.h2.Driver",
            password = ""
        )

        transaction(database) {
            SchemaUtils.create(sensorTable)
        }
    }
}