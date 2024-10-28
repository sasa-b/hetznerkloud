package tech.sco.hetznerkloud.model

import kotlinx.serialization.Serializable
import tech.sco.hetznerkloud.model.Server.Deprecation

@Serializable
data class Iso(
    val id: Long,
    val architecture: String,
    val deprecation: Deprecation,
    val description: String,
    val name: String,
    val type: String,
)
