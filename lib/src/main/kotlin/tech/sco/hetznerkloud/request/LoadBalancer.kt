@file:OptIn(ExperimentalSerializationApi::class)

package tech.sco.hetznerkloud.request

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import tech.sco.hetznerkloud.model.Labels
import tech.sco.hetznerkloud.model.LoadBalancer
import tech.sco.hetznerkloud.model.Network
import tech.sco.hetznerkloud.model.NetworkZone

@Serializable
data class CreateLoadBalancer(
    val algorithm: LoadBalancer.Algorithm,
    val labels: Labels,
    @SerialName("load_balancer_type")
    val loadBalancerType: String,
    val location: String,
    val name: String,
    val network: Network.Id,
    @SerialName("network_zone")
    val networkZone: NetworkZone,
    @SerialName("public_interface")
    val publicInterface: Boolean,
    val services: List<LoadBalancer.Service>,
    val targets: List<LoadBalancer.Target>,
) : HttpBody

@Serializable
data class AddService(
    @SerialName("destination_port")
    val destinationPort: Int,
    @SerialName("health_check")
    val healthCheck: LoadBalancer.Service.HealthCheck,
    val http: LoadBalancer.Service.Http,
    @SerialName("listen_port")
    val listenPort: Int,
    val protocol: LoadBalancer.Service.Protocol,
    @SerialName("proxyprotocol")
    val proxyProtocol: Boolean,
) : HttpBody

@Serializable
data class AddTarget(
    val ip: LoadBalancer.Target.Ip,
    @SerialName("label_selector")
    val labelSelector: LabelSelector,
    val server: LoadBalancer.Target.Server,
    val type: LoadBalancer.Target.Type,
    @SerialName("use_private_ip")
    val usePrivateIp: Boolean,
) : HttpBody

@Serializable
data class ChangeAlgorithm(val type: LoadBalancer.Algorithm.Type) : HttpBody

@Serializable
data class ChangeLoadBalancerProtection(val delete: Boolean) : HttpBody

@Serializable
data class ChangeLoadBalancerType(@SerialName("load_balancer_type") val loadBalancerType: String) : HttpBody
