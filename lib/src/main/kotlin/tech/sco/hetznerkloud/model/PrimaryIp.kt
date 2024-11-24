@file:OptIn(ExperimentalSerializationApi::class)

package tech.sco.hetznerkloud.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames
import tech.sco.hetznerkloud.serialization.OffsetDateTimeSerializer
import java.time.OffsetDateTime

@Serializable
data class PrimaryIp(
    val id: Id,
    @JsonNames("assignee_id")
    val assigneeId: Long,
    @JsonNames("assignee_type")
    val assigneeType: AssigneeType,
    @JsonNames("auto_delete")
    val autoDelete: Boolean,
    val blocked: Boolean,
    @Serializable(with = OffsetDateTimeSerializer::class)
    val created: OffsetDateTime,
    val datacenter: Datacenter,
    @JsonNames("dns_ptr")
    val dnsPtr: List<DnsPtr>,
    val ip: String,
    val labels: Labels,
    val name: String,
    val protection: Protection,
    val type: IpType,
) {
    @Serializable
    @JvmInline
    value class Id(override val value: Long) : ResourceId {
        override val type: ResourceType
            get() = ResourceType.PRIMARY_IP
    }

    @Serializable
    enum class AssigneeType {
        @SerialName("server")
        SERVER,
    }
}
