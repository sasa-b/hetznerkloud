package tech.sco.hetznerkloud.request

import kotlinx.serialization.Serializable
import tech.sco.hetznerkloud.model.Server

@Serializable
data class AssignFloatingIp(val server: Server.Id?) : HttpBody
