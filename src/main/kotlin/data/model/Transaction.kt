package data.model

class Transaction(
    val sensorId: Int,
    val prevIds: List<String>,
    val gasLevel: String,
    val epochTime: Long,
    val signedHash: String,
) {
    override fun equals(other: Any?) = (other as? Transaction)?.signedHash == signedHash

    override fun hashCode(): Int {
        var result = sensorId.hashCode()
        result = 31 * result + prevIds.hashCode()
        result = 31 * result + gasLevel.hashCode()
        result = 31 * result + epochTime.hashCode()
        result = 31 * result + signedHash.hashCode()
        return result
    }
}