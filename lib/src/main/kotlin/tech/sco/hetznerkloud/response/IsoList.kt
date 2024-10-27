package tech.sco.hetznerkloud.response

import kotlinx.serialization.Serializable
import tech.sco.hetznerkloud.model.read.Iso
import tech.sco.hetznerkloud.model.read.Meta

@Serializable
data class IsoList(
    val meta: Meta,
    val isos: List<Iso>,
)
