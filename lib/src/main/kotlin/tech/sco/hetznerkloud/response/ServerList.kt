package tech.sco.hetznerkloud.response

import kotlinx.serialization.Serializable
import tech.sco.hetznerkloud.model.read.Meta
import tech.sco.hetznerkloud.model.read.Server

@Serializable
data class ServerList(
    val meta: Meta,
    val servers: List<Server>,
)
