package data.model

data class Transaction(
    val sensorId: String,
    val prevId1: String,
    val prevId2: String,
    val vin: String,
    val gasLevel: Double,
    val signedHash: String,
    val timeStamp: String,
)