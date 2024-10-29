package tech.sco.hetznerkloud.response

import kotlinx.serialization.Serializable
import tech.sco.hetznerkloud.model.Iso

@Serializable
data class IsoItem(val iso: Iso)
