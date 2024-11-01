package tech.sco.hetznerkloud.request

import kotlinx.serialization.Serializable
import tech.sco.hetznerkloud.model.Labels

@Serializable
data class CreatePlacementGroup(val name: String, val type: String) : HttpBody

@Serializable
data class UpdatePlacementGroup(val name: String, val labels: Labels) : HttpBody
