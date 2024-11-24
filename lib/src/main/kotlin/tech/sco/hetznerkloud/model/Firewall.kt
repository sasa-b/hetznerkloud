@file:OptIn(ExperimentalSerializationApi::class)

package tech.sco.hetznerkloud.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames
import tech.sco.hetznerkloud.serialization.OffsetDateTimeSerializer
import java.time.OffsetDateTime

@Serializable
data class Firewall(
    val id: Id,
    @JsonNames("applied_to")
    val appliedTo: List<AppliedTo>,
    @Serializable(with = OffsetDateTimeSerializer::class)
    val created: OffsetDateTime,
    val labels: Labels,
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
        @JsonNames("destination_ips")
        val destinationIps: List<String>,
        val direction: Direction,
        val port: String,
        val protocol: Protocol,
        @JsonNames("source_ips")
        val sourceIps: List<String>,
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

        @Serializable
        data class LabelSelector(val selector: String)
    }
}
