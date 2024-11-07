package tech.sco.hetznerkloud.request

import kotlinx.serialization.Serializable
import tech.sco.hetznerkloud.model.Labels

sealed interface HttpBody

@Serializable
data class UpdateResource(
    val name: String,
    val labels: Labels,
) : HttpBody
