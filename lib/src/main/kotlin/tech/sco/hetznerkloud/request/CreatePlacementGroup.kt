package tech.sco.hetznerkloud.request

import kotlinx.serialization.Serializable

@Serializable
data class CreatePlacementGroup(
    val name: String,
    val type: String,
) : HttpBody
