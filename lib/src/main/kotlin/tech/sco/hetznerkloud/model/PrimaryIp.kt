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
    val assigneeType: String,
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
    val type: Type,
) {
    @Serializable
    @JvmInline
    value class Id(val value: Long)

    @Serializable
    data class DnsPtr(
        @JsonNames("dns_ptr")
        val dnsPtr: String,
        val ip: String,
    )

    @Serializable
    enum class Type {
        @SerialName("ipv4")
        IPV4,

        @SerialName("ipv6")
        IPV6,
    }
}
