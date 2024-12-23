@file:OptIn(ExperimentalSerializationApi::class)

package tech.sco.hetznerkloud.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import tech.sco.hetznerkloud.serialization.OffsetDateTimeSerializer
import java.time.OffsetDateTime

@Serializable
data class Action(
    val id: Id,
    val command: String,
    val error: Error? = null,
    @Serializable(with = OffsetDateTimeSerializer::class)
    val finished: OffsetDateTime? = null,
    val progress: Int = 0,
    val resources: List<Resource>,
    @Serializable(with = OffsetDateTimeSerializer::class)
    val started: OffsetDateTime,
    val status: Status,
) {
    @Serializable
    @JvmInline
    value class Id(override val value: Long) : ResourceId {
        override val type: ResourceType
            get() = ResourceType.ACTION
    }

    @Serializable
    enum class Status {
        @SerialName("running")
        RUNNING,

        @SerialName("success")
        SUCCESS,

        @SerialName("error")
        ERROR,
    }
}
