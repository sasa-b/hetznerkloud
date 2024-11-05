package tech.sco.hetznerkloud.model

import kotlinx.serialization.Serializable
import tech.sco.hetznerkloud.model.Server.Deprecation

@Serializable
data class Iso(
    val id: Id,
    val architecture: String?,
    val deprecation: Deprecation?,
    val description: String,
    val name: String?,
    val type: String?,
) {
    @Serializable
    @JvmInline
    value class Id(val value: Long)
}
