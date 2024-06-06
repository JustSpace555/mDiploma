package data.model

data class Sensor(
    val id: Int,
    val vin: String,
    val gasLiters: Float,
    val publicKey: ByteArray,
)