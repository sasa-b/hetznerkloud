package tech.sco.hetznerkloud.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import tech.sco.hetznerkloud.serialization.OffsetDateTimeSerializer
import java.time.OffsetDateTime

@Serializable
data class PlacementGroup(
    val id: Id,
    @Serializable(with = OffsetDateTimeSerializer::class)
    val created: OffsetDateTime,
    val labels: Labels,
    val name: String,
    val servers: List<Server.Id>,
    val type: Type,
) {
    @Serializable
    @JvmInline
    value class Id(val value: Long)

    @Serializable
    enum class Type {
        @SerialName("spread")
        SPREAD,
    }
}
