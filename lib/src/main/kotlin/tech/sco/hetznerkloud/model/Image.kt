@file:OptIn(ExperimentalSerializationApi::class)

package tech.sco.hetznerkloud.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames
import tech.sco.hetznerkloud.serialization.OffsetDateTimeSerializer
import java.time.OffsetDateTime

@Serializable
data class Image(
    val id: Id,
    val architecture: String,
    @JsonNames("bound_to")
    val boundTo: Long?,
    @Serializable(with = OffsetDateTimeSerializer::class)
    val created: OffsetDateTime,
    @JsonNames("created_from")
    val createdFrom: CreatedFrom?,
    @Serializable(with = OffsetDateTimeSerializer::class)
    val deleted: OffsetDateTime?,
    @Serializable(with = OffsetDateTimeSerializer::class)
    val deprecated: OffsetDateTime?,
    val description: String,
    @JsonNames("disk_size")
    val diskSize: Int,
    @JsonNames("image_size")
    val imageSize: Double?,
    val labels: Labels = emptyMap(),
    val name: String?,
    @JsonNames("os_flavor")
    val osFlavor: String,
    @JsonNames("os_version")
    val osVersion: String?,
    val protection: Protection,
    @JsonNames("rapid_deploy")
    val rapidDeploy: Boolean = false,
    val status: Status,
    val type: Type,
) {

    @Serializable
    @JvmInline
    value class Id(val value: Long)

    @Serializable
    data class CreatedFrom(
        val id: Long,
        val name: String,
    )

    enum class Status {
        @SerialName("available")
        AVAILABLE,

        @SerialName("avaicreatinglable")
        CREATING,

        @SerialName("unavailable")
        UNAVAILABLE,
    }

    // system app snapshot backup temporary
    enum class Type {
        @SerialName("system")
        SYSTEM,

        @SerialName("app")
        APP,

        @SerialName("snapshot")
        SNAPSHOT,

        @SerialName("backup")
        BACKUP,

        @SerialName("temporary")
        TEMPORARY,
    }
}
