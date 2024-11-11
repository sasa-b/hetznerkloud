@file:OptIn(ExperimentalSerializationApi::class)

package tech.sco.hetznerkloud.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames
import tech.sco.hetznerkloud.serialization.OffsetDateTimeSerializer
import java.time.OffsetDateTime

@Serializable
data class FloatingIp(
    val id: Id,
    val blocked: Boolean,
    @Serializable(with = OffsetDateTimeSerializer::class)
    val created: OffsetDateTime,
    val description: String?,
    @JsonNames("dns_ptr")
    val dnsPtr: List<DnsPtr>,
    @JsonNames("home_location")
    val homeLocation: Location,
    val ip: String,
    val labels: Labels,
    val name: String,
    val protection: Protection,
    val server: Server.Id?,
    val type: IpType,
) {
    @Serializable
    @JvmInline
    value class Id(val value: Long)
}
