@file:OptIn(ExperimentalSerializationApi::class)

package tech.sco.hetznerkloud.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames
import tech.sco.hetznerkloud.serialization.OffsetDateTimeSerializer
import java.time.OffsetDateTime

@Serializable
data class Volume(
    val id: Id,
    @Serializable(with = OffsetDateTimeSerializer::class)
    val created: OffsetDateTime,
    val format: String,
    val labels: Labels,
    @JsonNames("linux_device")
    val linuxDevice: String,
    val location: Location,
    val name: String,
    val protection: Protection,
    val server: Server.Id,
    val size: Long,
    val status: String,
) {
    @Serializable
    @JvmInline
    value class Id(val value: Long)
}
