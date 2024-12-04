package tech.sco.hetznerkloud.request

import kotlinx.serialization.Serializable
import tech.sco.hetznerkloud.model.Labels

@Serializable
data class UpdateImage(val description: String, val labels: Labels, val type: String) : HttpBody
