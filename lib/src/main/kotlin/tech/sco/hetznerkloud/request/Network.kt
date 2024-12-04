package tech.sco.hetznerkloud.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import tech.sco.hetznerkloud.model.Labels
import tech.sco.hetznerkloud.model.Network
import tech.sco.hetznerkloud.model.NetworkZone

@Serializable
data class CreateNetwork(
    @SerialName("expose_routes_to_vswitch")
    val exposeRoutesToVSwitch: Boolean,
    @SerialName("ip_range")
    val ipRange: String,
    val labels: Labels,
    val name: String,
    val routes: List<Network.Route>,
    val subnets: List<Subnet>,
) : HttpBody {
    @Serializable
    data class Subnet(
        @SerialName("ip_range")
        val ipRange: String? = null,
        @SerialName("network_zone")
        val networkZone: NetworkZone,
        val type: Network.Type,
        @SerialName("vswitch_id")
        val vSwitchId: Long? = null,
    )
}

@Serializable
data class UpdateNetwork(
    @SerialName("expose_routes_to_vswitch")
    val exposeRoutesToVSwitch: Boolean,
    val labels: Labels,
    val name: String,
) : HttpBody

object AttachToNetwork {
    @Serializable
    data class Server(
        val network: Network.Id,
        @SerialName("alias_ips")
        val aliasIps: List<String>? = null,
        val ip: String? = null,
    ) : HttpBody

    @Serializable
    data class LoadBalancer(val network: Network.Id, val ip: String? = null) : HttpBody
}

@Serializable
data class AddRoute(val destination: String, val gateway: String) : HttpBody

@Serializable
data class AddSubnet(
    @SerialName("ip_range")
    val ipRange: String? = null,
    @SerialName("network_zone")
    val networkZone: NetworkZone,
    val type: Network.Type,
    @SerialName("vswitch_id")
    val vSwitchId: Long? = null,
) : HttpBody

@Serializable
data class ChangeIpRange(
    @SerialName("ip_range")
    val ipRange: String,
) : HttpBody

@Serializable
data class DeleteSubnet(@SerialName("ip_range") val ipRange: String) : HttpBody

@Serializable
data class DeleteRoute(val destination: String, val gateway: String) : HttpBody
