@file:OptIn(ExperimentalSerializationApi::class)

package tech.sco.hetznerkloud.request

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames
import tech.sco.hetznerkloud.model.Labels
import tech.sco.hetznerkloud.model.LoadBalancer
import tech.sco.hetznerkloud.model.Network
import tech.sco.hetznerkloud.model.NetworkZone

@Serializable
data class CreateLoadBalancer(
    val algorithm: LoadBalancer.Algorithm,
    val labels: Labels,
    @JsonNames("load_balancer_type")
    val loadBalancerType: String,
    val location: String,
    val name: String,
    val network: Network.Id,
    val networkZone: NetworkZone,
    @JsonNames("public_interface")
    val publicInterface: Boolean,
    val services: List<LoadBalancer.Service>,
    val targets: List<LoadBalancer.Target>,
) : HttpBody
