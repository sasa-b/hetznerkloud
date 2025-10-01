@file:OptIn(ExperimentalSerializationApi::class)

package tech.sco.hetznerkloud.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames
import tech.sco.hetznerkloud.serialization.OffsetDateTimeSerializer
import java.time.OffsetDateTime

@Serializable
data class Subaccount(
    val id: Id,
    val username: String,
    @JsonNames("home_directory")
    val homeDirectory: String,
    val server: String,
    @JsonNames("access_settings")
    val accessSettings: AccessSettings,
    val description: String,
    val labels: Labels,
    @Serializable(OffsetDateTimeSerializer::class)
    val created: OffsetDateTime,
    @JsonNames("storage_box")
    val storageBox: StorageBox.Id,
) {
    @Serializable
    @JvmInline
    value class Id(override val value: Long) : ResourceId {
        override val type: ResourceType
            get() = ResourceType.SUBACCOUNT
    }

    @Serializable
    data class AccessSettings(
        @JsonNames("reachable_externally")
        val reachableExternally: Boolean,
        val readonly: Boolean,
        @JsonNames("samba_enabled")
        val sambaEnabled: Boolean,
        @JsonNames("ssh_enabled")
        val sshEnabled: Boolean,
        @JsonNames("webdav_enabled")
        val webdavEnabled: Boolean,
    )
}
