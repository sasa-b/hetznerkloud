@file:OptIn(ExperimentalSerializationApi::class)

package tech.sco.hetznerkloud.response

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames
import tech.sco.hetznerkloud.model.Datacenter
import tech.sco.hetznerkloud.model.Meta

@Serializable
data class DatacenterItems(
    val meta: Meta,
    @JsonNames("datacenters")
    val items: List<Datacenter>,
    val recommendation: Long,
) : Collection<Datacenter> by items
