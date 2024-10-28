@file:OptIn(ExperimentalSerializationApi::class)

package tech.sco.hetznerkloud.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames
import tech.sco.hetznerkloud.serialization.OffsetDateTimeSerializer
import java.time.OffsetDateTime

@Serializable
data class Server(
    val id: Int,
    @JsonNames("backup_window")
    val backupWindow: String,
    @Serializable(with = OffsetDateTimeSerializer::class)
    val created: OffsetDateTime,
    val datacenter: Datacenter,
    val image: Image,
    @JsonNames("included_traffic")
    val includedTraffic: Long,
    @JsonNames("ingoing_traffic")
    val ingoingTraffic: Long,
    val iso: Iso,
    val labels: Labels = emptyMap(),
    @JsonNames("load_balancers")
    val loadBalancers: List<Int>,
    val locked: Boolean,
    val name: String,
    @JsonNames("outgoing_traffic")
    val outgoingTraffic: Long,
    @JsonNames("placement_group")
    val placementGroup: PlacementGroup? = null,
    @JsonNames("primary_disk_size")
    val primaryDiskSize: Long,
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
        @Serializable(with = OffsetDateTimeSerializer::class)
        val announced: OffsetDateTime,
        @JsonNames("unavailable_after")
        @Serializable(with = OffsetDateTimeSerializer::class)
        val unavailableAfter: OffsetDateTime,
    )

    @Serializable
    data class PlacementGroup(
        val id: Int,
        @Serializable(with = OffsetDateTimeSerializer::class)
        val created: OffsetDateTime,
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
            val id: Int? = null,
            val blocked: Boolean,
            @JsonNames("dns_ptr")
            val dnsPtr: String,
            val ip: String,
        )

        @Serializable
        data class Ipv6(
            val id: Int? = null,
            val blocked: Boolean,
            @JsonNames("dns_ptr")
            val dnsPtr: List<Map<String, String>>,
            val ip: String,
        )
    }
}
