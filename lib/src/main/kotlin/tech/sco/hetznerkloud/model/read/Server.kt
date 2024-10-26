@file:OptIn(ExperimentalSerializationApi::class)

package tech.sco.hetznerkloud.model.read

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
data class Server(
    val id: Int,
    @JsonNames("backup_window")
    val backupWindow: String,
    val created: String,
    val datacenter: DataCenter,
    val image: Image,
    @JsonNames("included_traffic")
    val includedTraffic: Int,
    @JsonNames("ingoing_traffic")
    val ingoingTraffic: Int,
    val iso: Iso,
    val labels: Labels,
    @JsonNames("load_balancers")
    val loadBalancers: List<Int>,
    val locked: Boolean,
    val name: String,
    @JsonNames("outgoing_traffic")
    val outgoingTraffic: Int,
    @JsonNames("placement_group")
    val placementGroup: PlacementGroup,
    @JsonNames("primary_disk_size")
    val primaryDiskSize: Int,
    @JsonNames("private_net")
    val privateNet: List<PrivateNetwork>,
    val protection: Protection,
    @JsonNames("public_net")
    val publicNet: PublicNetwork,
    @JsonNames("rescue_enabled")
    val rescueEnabled: Boolean,
    @JsonNames("server_type")
    val serverType: ServerType,
    val status: String,
    val volumes: List<Int>,
) {
    @Serializable
    data class Deprecation(
        val announced: String,
        @JsonNames("unavailable_after")
        val unavailableAfter: String,
    )

    @Serializable
    data class PlacementGroup(
        val id: Int,
        val created: String,
        val labels: Labels,
        val name: String,
        val servers: List<Int>,
        val type: String,
    )

    @Serializable
    data class PrivateNetwork(
        @JsonNames("alias_ips")
        val aliasIps: List<String>,
        val ip: String,
        @JsonNames("mac_address")
        val macAddress: String,
        val network: Int,
    )

    @Serializable
    data class Protection(
        val delete: Boolean,
        val rebuild: Boolean,
    )

    @Serializable
    data class PublicNetwork(
        val firewalls: List<Firewall>,
        @JsonNames("floating_ips")
        val floatingIps: List<Int>,
        val ipv4: Ipv4,
        val ipv6: Ipv6,
    ) {
        @Serializable
        data class Firewall(
            val id: Int,
            val status: String,
        )

        @Serializable
        data class Ipv4(
            val id: Int,
            val blocked: Boolean,
            @JsonNames("dns_ptr")
            val dnsPtr: String,
            val ip: String,
        )

        @Serializable
        data class Ipv6(
            val id: Int,
            val blocked: Boolean,
            @JsonNames("dns_ptr")
            val dnsPtr: List<Map<String, String>>,
            val ip: String,
        )
    }
}
