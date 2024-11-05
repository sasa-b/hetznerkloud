@file:OptIn(ExperimentalSerializationApi::class)

package tech.sco.hetznerkloud.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames
import tech.sco.hetznerkloud.serialization.OffsetDateTimeSerializer
import java.time.OffsetDateTime
import tech.sco.hetznerkloud.model.Server.Id as ServerId

@Serializable
data class LoadBalancer(
    val id: Id,
    val algorithm: Algorithm,
    @Serializable(with = OffsetDateTimeSerializer::class)
    val created: OffsetDateTime,
    @JsonNames("included_traffic")
    val includedTraffic: Long,
    @JsonNames("ingoing_traffic")
    val ingoingTraffic: Long?,
    val labels: Labels,
    @JsonNames("load_balancer_type")
    val loadBalancerType: LoadBalancerType,
    val location: Location,
    val name: String,
    // This one is marked as required in API docs ???
    val outgoingTraffic: Long? = null,
    @JsonNames("private_net")
    val privateNet: List<PrivateNetwork>,
    val protection: Protection,
    @JsonNames("public_net")
    val publicNet: PublicNetwork,
    val services: List<Service>,
    val targets: List<Target>,
) {
    @Serializable
    @JvmInline
    value class Id(val value: Long)

    @Serializable
    data class Algorithm(val type: Type) {

        enum class Type {
            @SerialName("round_robin")
            ROUND_ROBIN,

            @SerialName("least_connections")
            LEAST_CONNECTIONS,
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
            val dnsPtr: String? = null,
            val ip: String? = null,
        )
    }

    @Serializable
    data class Service(
        @JsonNames("destination_port")
        val destinationPort: Int,
        @JsonNames("health_check")
        val healthCheck: HealthCheck,
        val http: Http? = null,
        @JsonNames("listen_port")
        val listenPort: Int,
        val protocol: Protocol,
        @JsonNames("proxyprotocol")
        val proxyProtocol: Boolean,
    ) {
        @Serializable
        data class HealthCheck(
            val http: Http,
            val interval: Int,
            val port: Int,
            val protocol: Protocol,
            val retries: Int,
            val timeout: Int,
        ) {
            @Serializable
            data class Http(
                val domain: String?,
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
            val cookieLifetime: Int = 300,
            @JsonNames("cookie_name")
            val cookieName: String = "HCLBSTICKY",
            @JsonNames("redirect_http")
            val redirectHttp: Boolean = false,
            @JsonNames("sticky_sessions")
            val stickySessions: Boolean = false,
        )

        enum class Protocol {
            @SerialName("tcp")
            TCP,

            @SerialName("http")
            HTTP,

            @SerialName("https")
            HTTPS,
        }
    }

    @Serializable
    data class Target(
        @JsonNames("health_status")
        val healthStatus: List<HealthStatus> = emptyList(),
        val ip: Ip? = null,
        @JsonNames("label_selector")
        val labelSelector: Map<String, String> = emptyMap(),
        val server: Server? = null,
        val targets: List<Target> = emptyList(),
        val type: Type,
        @JsonNames("use_private_ip")
        val usePrivateIp: Boolean = false,
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
        data class Server(val id: ServerId)

        enum class Type {
            @SerialName("server")
            SERVER,

            @SerialName("ip")
            IP,

            @SerialName("label_selector")
            LABEL_SELECTOR,
        }
    }
}
