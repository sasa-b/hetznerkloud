@file:OptIn(ExperimentalSerializationApi::class)

package tech.sco.hetznerkloud.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames
import tech.sco.hetznerkloud.serialization.OffsetDateTimeSerializer
import java.time.OffsetDateTime

@Serializable
data class LoadBalancer(
    val id: Id,
    val algorithm: Algorithm,
    @Serializable(with = OffsetDateTimeSerializer::class)
    val created: OffsetDateTime,
    @JsonNames("included_traffic")
    val includedTraffic: Long,
    @JsonNames("ingoing_traffic")
    val ingoingTraffic: Long,
    val labels: Labels,
    @JsonNames("load_balancer_type")
    val loadBalancerType: LoadBalancerType,
    val location: Location,
    val name: String,
    val outgoingTraffic: Long? = null,
    @JsonNames("private_net")
    val privateNet: List<PrivateNetwork>,
    val protection: Protection,
    @JsonNames("public_net")
    val publicNet: List<PrivateNetwork>,
    val services: List<Service>,
    val targets: List<Target>,
) {
    @Serializable
    data class Id(override val value: Long) : ResourceId()

    @Serializable
    data class Algorithm(val type: Type) {

        enum class Type(val value: String) {
            ROUND_ROBIN("round_robin"),
            LEAST_CONNECTIONS("least_connections"),
        }
    }

    @Serializable
    data class PrivateNetwork(
        val ip: String,
        val network: Network.Id,
    )

    @Serializable
    data class PublicNetwork(
        val enabled: Boolean,
        val ipv4: Ip,
        val ipv6: Ip,
    ) {

        @Serializable
        data class Ip(
            @JsonNames("dns_ptr")
            val dnsPtr: String,
            val ip: String,
        )
    }

    @Serializable
    data class Service(
        @JsonNames("destination_port")
        val destinationPort: Int,
        @JsonNames("health_check")
        val healthCheck: HealthCheck,
        val http: Http,
        @JsonNames("listen_port")
        val listenPort: Int,
        val protocol: String,
        @JsonNames("proxyprotocol")
        val proxyProtocol: Boolean,
    ) {
        @Serializable
        data class HealthCheck(
            val http: Http,
            val interval: Int,
            val port: Int,
            val protocol: String,
            val retries: Int,
            val timeout: Int,
        ) {
            @Serializable
            data class Http(
                val domain: String,
                val path: String,
                val response: String,
                @JsonNames("status_codes")
                val statusCodes: List<String>,
                val tls: Boolean,
            )
        }

        @Serializable
        data class Http(
            val certificates: List<Certificate.Id>,
            @JsonNames("cookie_lifetime")
            val cookieLifetime: Int,
            @JsonNames("cookie_name")
            val cookieName: String,
            @JsonNames("redirect_http")
            val redirectHttp: Boolean,
            @JsonNames("sticky_session")
            val stickySession: Boolean,
        )
    }

    @Serializable
    data class Target(
        val healthStatus: HealthStatus,
        val ip: Ip,
        @JsonNames("label_selector")
        val labelSelector: Map<String, String>,
        val server: Server,
        val targets: List<Target>,
        val type: String,
        @JsonNames("use_private_ip")
        val usePrivateIp: Boolean,
    ) {
        @Serializable
        data class HealthStatus(
            @JsonNames("listen_port")
            val listenPort: Int,
            val status: String,
        )

        @Serializable
        data class Ip(val ip: String)

        @Serializable
        data class Server(val id: Long)
    }
}
