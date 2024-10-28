package tech.sco.hetznerkloud.response

import kotlinx.serialization.Serializable
import tech.sco.hetznerkloud.model.Datacenter
import tech.sco.hetznerkloud.model.Meta

@Serializable
data class DatacenterList(
    val meta: Meta,
    val datacenters: List<Datacenter>,
    val recommendation: Int,
)
