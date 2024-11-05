package tech.sco.hetznerkloud.request

import kotlinx.serialization.Serializable
import tech.sco.hetznerkloud.model.Labels

@Serializable
data class CreateVolume(
    val automount: Boolean,
    val format: String,
    val labels: Labels,
    val location: String,
    val name: String,
    val size: Long,
) : HttpBody

@Serializable
data class UpdateVolume(
    val labels: Labels,
    val name: String,
) : HttpBody
