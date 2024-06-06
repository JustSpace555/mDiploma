package domain.model

import kotlinx.serialization.Serializable

@Serializable
data class SensorDto(val id: Int?, val vin: String?, val gasLiters: Float?, val publicKey: String?)