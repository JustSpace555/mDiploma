package domain

import kotlinx.serialization.Serializable

@Serializable
data class SensorDto(val id: Int?, val vin: String?, val gasLiters: Int?, val publicKey: String?)