package tech.sco.hetznerkloud.response

import kotlinx.serialization.Serializable
import tech.sco.hetznerkloud.model.read.DataCenter
import tech.sco.hetznerkloud.model.read.Meta

@Serializable
data class DataCenterList(
    val meta: Meta,
    val datacenters: List<DataCenter>,
    val recommendation: Int,
)
