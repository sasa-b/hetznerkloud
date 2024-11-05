package tech.sco.hetznerkloud.model

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
    val status: String,
) {
    @Serializable
    @JvmInline
    value class Id(val value: Long)
}
