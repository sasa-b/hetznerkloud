package tech.sco.hetznerkloud.response

import kotlinx.serialization.Serializable
import tech.sco.hetznerkloud.model.Meta
import tech.sco.hetznerkloud.model.Network

@Serializable
data class NetworkList(
    val meta: Meta,
    val networks: List<Network>,
) 
