@file:OptIn(ExperimentalSerializationApi::class)

package tech.sco.hetznerkloud.request

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames
import tech.sco.hetznerkloud.model.Labels
import tech.sco.hetznerkloud.model.Network

@Serializable
data class CreateNetwork(
    @JsonNames("expose_routes_to_vswitch")
    val exposeRoutesToVSwitch: Boolean,
    @JsonNames("ip_range")
    val ipRange: String,
    val labels: Labels,
    val name: String,
    val routes: List<Network.Route>,
    val subnets: List<Network.Subnet>,
) : HttpBody
