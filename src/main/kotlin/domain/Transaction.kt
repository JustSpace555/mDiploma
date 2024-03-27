package domain

data class Transaction(
    val pow: String,
    val prevId1: String,
    val prevId2: String,
    val vin: String,
    val gasLevel: Double,
    val signature: String,
)