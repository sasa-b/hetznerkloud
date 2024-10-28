package tech.sco.hetznerkloud.response

import kotlinx.serialization.Serializable
import tech.sco.hetznerkloud.model.Server

@Serializable
data class ServerItem(val server: Server)
