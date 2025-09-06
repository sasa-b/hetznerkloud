@file:OptIn(ExperimentalSerializationApi::class)

package tech.sco.hetznerkloud.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames
import tech.sco.hetznerkloud.serialization.OffsetDateTimeSerializer
import java.time.OffsetDateTime

@Serializable
data class StorageBox(
    val id: Id,
    val username: String?,
    val status: Status,
    val name: String,
    @JsonNames("storage_box_type")
    val storageBoxType: StorageBoxType,
    val location: Location,
    @JsonNames("access_settings")
    val accessSettings: AccessSettings,
    val server: String?,
    val system: String?,
    val stats: Stats?,
    val labels: Labels,
    val protection: Protection,
    @JsonNames("snapshot_plan")
    val snapshotPlan: SnapshotPlan?,
    @Serializable(with = OffsetDateTimeSerializer::class)
    val created: OffsetDateTime,
) {
    @Serializable
    @JvmInline
    value class Id(override val value: Long) : ResourceId {
        override val type: ResourceType
            get() = ResourceType.STORAGE_BOX
    }

    @Serializable
    enum class Status {

        @SerialName("active")
        ACTIVE,

        @SerialName("initializing")
        INITIALIZING,

        @SerialName("locked")
        LOCKED,
    }

    @Serializable
    data class AccessSettings(
        @JsonNames("reachable_externally")
        val reachableExternally: Boolean,
        @JsonNames("samba_enabled")
        val sambaEnabled: Boolean,
        @JsonNames("ssh_enabled")
        val sshEnabled: Boolean,
        @JsonNames("webdav_enabled")
        val webdavEnabled: Boolean,
        @JsonNames("zfs_enabled")
        val zfsEnabled: Boolean,
    )

    @Serializable
    data class Stats(
        val size: Long,
        @JsonNames("size_data")
        val sizeData: Long,
        @JsonNames("size_snapshots")
        val sizeSnapshots: Long,
    )

    @Serializable
    data class SnapshotPlan(
        @JsonNames("max_snapshots")
        val maxSnapshots: Int,
        val minute: Int?,
        val hour: Int?,
        @JsonNames("day_of_week")
        val dayOfWeek: Int?,
        @JsonNames("day_of_month")
        val dayOfMonth: Int?,
    )
}
