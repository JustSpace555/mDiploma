package domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TransactionDto(
    @SerialName("id")
    val sensorId: Int?,
    val oldTransactions: List<String>?,
    val gasLevel: String?,
    val timeStamp: Long?,
    val signedHash: String?,
)