@file:OptIn(ExperimentalSerializationApi::class)

package tech.sco.hetznerkloud.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames
import tech.sco.hetznerkloud.serialization.OffsetDateTimeSerializer
import java.time.OffsetDateTime

@Serializable
data class Network(
    val id: Id,
    @Serializable(with = OffsetDateTimeSerializer::class)
    val created: OffsetDateTime,
    @JsonNames("expose_routes_to_vswitch")
    val exposeRoutesToVSwitch: Boolean,
    @JsonNames("ip_range")
    val ipRange: String,
    val labels: Labels,
    @JsonNames("load_balancers")
    val loadBalancers: List<LoadBalancer.Id> = emptyList(),
    val name: String,
    val protection: Protection,
    val routes: List<Route>,
    val servers: List<Server.Id>,
    val subnets: List<Subnet>,
) {
    @Serializable
    @JvmInline
    value class Id(val value: Long)

    @Serializable
    data class Route(
        val destination: String,
        val gateway: String,
    )

    @Serializable
    data class Subnet(
        val gateway: String,
        @JsonNames("ip_range")
        val ipRange: String? = null,
        @JsonNames("network_zone")
        val networkZone: NetworkZone,
        val type: Type,
        @JsonNames("vswitch_id")
        val vSwitchId: Long? = null,
    )

    enum class Type {
        @SerialName("cloud")
        CLOUD,

        @Deprecated("was used to connect only cloud Servers into your Network. This type is deprecated and is replaced by type cloud")
        @SerialName("server")
        SERVER,

        @SerialName("vswitch")
        V_SWITCH,
    }
}
