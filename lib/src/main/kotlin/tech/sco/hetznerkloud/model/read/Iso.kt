package tech.sco.hetznerkloud.model.read

import kotlinx.serialization.Serializable
import tech.sco.hetznerkloud.model.read.Server.Deprecation

@Serializable
data class Iso(
    val id: Int,
    val architecture: String,
    val deprecation: Deprecation,
    val description: String,
    val name: String,
    val type: String,
)
