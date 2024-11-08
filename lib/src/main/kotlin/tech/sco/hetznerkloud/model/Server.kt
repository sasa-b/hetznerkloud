@file:OptIn(ExperimentalSerializationApi::class)

package tech.sco.hetznerkloud.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames
import tech.sco.hetznerkloud.serialization.OffsetDateTimeSerializer
import tech.sco.hetznerkloud.serialization.TimeSeriesValuePairSerializer
import java.time.OffsetDateTime

@Serializable
data class Server(
    val id: Id,
    @JsonNames("backup_window")
    val backupWindow: String?,
    @Serializable(with = OffsetDateTimeSerializer::class)
    val created: OffsetDateTime,
    val datacenter: Datacenter,
    val image: Image?,
    @JsonNames("included_traffic")
    val includedTraffic: Long?,
    @JsonNames("ingoing_traffic")
    val ingoingTraffic: Long?,
    val iso: Iso?,
    val labels: Labels = emptyMap(),
    @JsonNames("load_balancers")
    val loadBalancers: List<Int> = emptyList(),
    val locked: Boolean,
    val name: String,
    @JsonNames("outgoing_traffic")
    val outgoingTraffic: Long?,
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
    val status: Status,
    val volumes: List<Int>,
) {
    @Serializable
    @JvmInline
    value class Id(val value: Long)

    @Serializable
    enum class Status {
        @SerialName("running")
        RUNNING,

        @SerialName("initializing")
        INITIALIZING,

        @SerialName("starting")
        STARTING,

        @SerialName("stopping")
        STOPPING,

        @SerialName("off")
        OFF,

        @SerialName("deleting")
        DELETING,

        @SerialName("migrating")
        MIGRATING,

        @SerialName("rebuilding")
        REBUILDING,

        @SerialName("unknown")
        UNKNOWN,
    }

    @Serializable
    data class Deprecation(
        @Serializable(with = OffsetDateTimeSerializer::class)
        val announced: OffsetDateTime,
        @JsonNames("unavailable_after")
        @Serializable(with = OffsetDateTimeSerializer::class)
        val unavailableAfter: OffsetDateTime,
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
            val id: Long,
            val status: String,
        )

        @Serializable
        data class Ipv4(
            val id: Long? = null,
            val blocked: Boolean,
            @JsonNames("dns_ptr")
            val dnsPtr: String,
            val ip: String,
        )

        @Serializable
        data class Ipv6(
            val id: Long? = null,
            val blocked: Boolean,
            @JsonNames("dns_ptr")
            val dnsPtr: List<Map<String, String>>,
            val ip: String,
        )
    }
}

@Serializable
data class ServerMetrics(
    @Serializable(with = OffsetDateTimeSerializer::class)
    val end: OffsetDateTime,
    @Serializable(with = OffsetDateTimeSerializer::class)
    val start: OffsetDateTime,
    val step: Long,
    @JsonNames("time_series")
    val timeSeries: TimeSeries,
) {
    @Serializable
    enum class Type(val value: String) {
        CPU("cpu"),
        NETWORK("network"),
        DISK("disk"),
    }

    @Serializable
    data class TimeSeries(
        val cpu: Element = Element(),
        @JsonNames("disk.0.iops.read")
        val diskIoPerSecondsRead: Element = Element(),
        @JsonNames("disk.0.iops.write")
        val diskIoPerSecondsWrite: Element = Element(),
        @JsonNames("disk.0.bandwidth.read")
        val diskBandwidthRead: Element = Element(),
        @JsonNames("disk.0.bandwidth.write")
        val diskBandwidthWrite: Element = Element(),
        @JsonNames("network.0.pps.in")
        val networkPacketsPerSecondIn: Element = Element(),
        @JsonNames("network.0.pps.out")
        val networkPacketsPerSecondOut: Element = Element(),
        @JsonNames("network.0.bandwidth.in")
        val networkBandwidthIn: Element = Element(),
        @JsonNames("network.0.bandwidth.out")
        val networkBandwidthOut: Element = Element(),
    ) {
        @Serializable
        data class Element(val values: List<ValuePair> = emptyList())

        @Serializable(with = TimeSeriesValuePairSerializer::class)
        data class ValuePair(val first: Double, val second: String)
    }
}
