package domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TransactionDto(
    @SerialName("id")
    val sensorId: String?,
    val pow: String?,
    val prevId1: String?,
    val prevId2: String?,
    val vin: String?,
    val gasLevel: Double?,
    val signedHash: String?,
)