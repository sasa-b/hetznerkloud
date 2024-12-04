package tech.sco.hetznerkloud.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import tech.sco.hetznerkloud.model.Labels
import tech.sco.hetznerkloud.model.Network

sealed interface HttpBody

@Serializable
data class UpdateResource(val name: String, val labels: Labels) : HttpBody

@Serializable
data class LabelSelector(val selector: String)

@Serializable
data class DetachFromNetwork(val network: Network.Id) : HttpBody

@Serializable
data class ChangeReverseDns(
    @SerialName("dns_ptr")
    val dnsPtr: String,
    val ip: String,
) : HttpBody

@Serializable
data class ChangeDeleteProtection(val delete: Boolean) : HttpBody
