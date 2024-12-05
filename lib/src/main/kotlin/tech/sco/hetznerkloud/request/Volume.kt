package tech.sco.hetznerkloud.request

import kotlinx.serialization.Serializable
import tech.sco.hetznerkloud.model.Labels
import tech.sco.hetznerkloud.model.Server

@Serializable
data class CreateVolume(val automount: Boolean, val format: String, val labels: Labels, val location: String, val name: String, val size: Long) : HttpBody

@Serializable
data class AttachToServer(val automount: Boolean, val server: Server.Id) : HttpBody

@Serializable
data class ResizeVolume(val size: Long) : HttpBody
