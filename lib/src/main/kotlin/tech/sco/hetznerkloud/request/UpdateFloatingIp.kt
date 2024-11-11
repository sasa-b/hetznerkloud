package tech.sco.hetznerkloud.request

import kotlinx.serialization.Serializable
import tech.sco.hetznerkloud.model.Labels

@Serializable
data class UpdateFloatingIp(
    val name: String,
    val labels: Labels,
    val description: String,
) : HttpBody
