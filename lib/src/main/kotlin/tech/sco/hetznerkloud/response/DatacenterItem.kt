package tech.sco.hetznerkloud.response

import kotlinx.serialization.Serializable
import tech.sco.hetznerkloud.model.Datacenter

@Serializable
data class DatacenterItem(
    val datacenter: Datacenter,
)
