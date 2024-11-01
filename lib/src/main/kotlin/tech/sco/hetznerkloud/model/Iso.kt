package tech.sco.hetznerkloud.model

import kotlinx.serialization.Serializable
import tech.sco.hetznerkloud.model.Server.Deprecation
import tech.sco.hetznerkloud.serialization.IsoIdSerializer

@Serializable
data class Iso(
    val id: Id,
    val architecture: String?,
    val deprecation: Deprecation?,
    val description: String,
    val name: String?,
    val type: String?,
) {
    @Serializable(with = IsoIdSerializer::class)
    data class Id(override val value: Long) : ResourceId()
}
