@file:OptIn(ExperimentalSerializationApi::class)

package tech.sco.hetznerkloud.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames
import tech.sco.hetznerkloud.request.LabelSelector
import tech.sco.hetznerkloud.serialization.OffsetDateTimeSerializer
import java.time.OffsetDateTime

@Serializable
data class Firewall(
    val id: Id,
    @JsonNames("applied_to")
    val appliedTo: List<AppliedTo>,
    @Serializable(with = OffsetDateTimeSerializer::class)
    val created: OffsetDateTime,
    val labels: Labels? = null,
    val name: String,
    val rules: List<Rule>,
) {
    @Serializable
    @JvmInline
    value class Id(override val value: Long) : ResourceId {
        override val type: ResourceType
            get() = ResourceType.FIREWALL
    }

    @Serializable
    data class Rule(
        val description: String?,
        @SerialName("destination_ips")
        val destinationIps: List<String>? = null,
        val direction: Direction,
        val port: String? = null,
        val protocol: Protocol,
        @SerialName("source_ips")
        val sourceIps: List<String>? = null,
    )

    @Serializable
    enum class Direction {
        @SerialName("in")
        IN,

        @SerialName("out")
        OUT,
    }

    @Serializable
    enum class Protocol {
        @SerialName("tcp")
        TCP,

        @SerialName("udp")
        UDP,

        @SerialName("icmp")
        ICMP,

        @SerialName("esp")
        ESP,

        @SerialName("gre")
        GRE,
    }

    @Serializable
    data class AppliedTo(
        @JsonNames("applied_to_resources")
        val appliedToResources: List<Resource>,
        @JsonNames("label_selector")
        val labelSelector: LabelSelector,
        val server: ServerResource,
        val type: String,
    ) {
        @Serializable
        data class Resource(val server: ServerResource, val type: String)

        @Serializable
        @SerialName("server")
        data class ServerResource(val id: Server.Id)
    }
}
