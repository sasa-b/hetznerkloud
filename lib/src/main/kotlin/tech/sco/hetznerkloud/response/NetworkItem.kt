package tech.sco.hetznerkloud.response

import kotlinx.serialization.Serializable
import tech.sco.hetznerkloud.model.Network

@Serializable
data class NetworkItem(
    val network: Network,
)
