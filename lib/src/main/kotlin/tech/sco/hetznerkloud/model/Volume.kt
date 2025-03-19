@file:OptIn(ExperimentalSerializationApi::class)

package tech.sco.hetznerkloud.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
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
    val server: Server.Id?,
    val size: Long,
    val status: Status,
) {
    @Serializable
    @JvmInline
    value class Id(override val value: Long) : ResourceId {
        override val type: ResourceType
            get() = ResourceType.VOLUME
    }

    @Serializable
    enum class Status {
        @SerialName("creating")
        CREATING,

        @SerialName("available")
        AVAILABLE,
    }
}
