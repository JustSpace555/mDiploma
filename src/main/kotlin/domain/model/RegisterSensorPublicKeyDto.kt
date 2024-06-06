package domain.model

import kotlinx.serialization.Serializable

@Serializable
data class RegisterSensorPublicKeyDto(val publicKey: IntArray?)