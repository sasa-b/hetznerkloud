@file:OptIn(ExperimentalSerializationApi::class)

package tech.sco.hetznerkloud.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames
import tech.sco.hetznerkloud.serialization.OffsetDateTimeSerializer
import java.time.OffsetDateTime

@Serializable
data class SSHKey(
    val id: Id,
    @Serializable(with = OffsetDateTimeSerializer::class)
    val created: OffsetDateTime,
    val fingerprint: String,
    val labels: Labels = emptyMap(),
    val name: String,
    @JsonNames("public_key")
    val publicKey: String,
) {
    @Serializable
    @JvmInline
    value class Id(val value: Long)
}
