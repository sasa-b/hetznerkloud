package tech.sco.hetznerkloud.model

import kotlinx.serialization.Serializable
import tech.sco.hetznerkloud.serialization.OffsetDateTimeSerializer
import tech.sco.hetznerkloud.serialization.PlacementGroupIdSerializer
import java.time.OffsetDateTime

@Serializable
data class PlacementGroup(
    val id: Id,
    @Serializable(with = OffsetDateTimeSerializer::class)
    val created: OffsetDateTime,
    val labels: Labels,
    val name: String,
    val servers: List<Server.Id>,
    val type: String,
) {
    @Serializable(with = PlacementGroupIdSerializer::class)
    data class Id(override val value: Long) : ResourceId()
}
