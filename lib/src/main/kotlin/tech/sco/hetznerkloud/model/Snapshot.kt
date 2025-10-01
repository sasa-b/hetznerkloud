@file:OptIn(ExperimentalSerializationApi::class)

package tech.sco.hetznerkloud.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames
import tech.sco.hetznerkloud.serialization.OffsetDateTimeSerializer
import java.time.OffsetDateTime

@Serializable
data class Snapshot(
    val id: Id,
    val name: String,
    val description: String?,
    val stats: Stats,
    @JsonNames("is_automatic")
    val isAutomatic: Boolean,
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
            get() = ResourceType.SNAPSHOT
    }

    @Serializable
    data class Stats(
        val size: Long,
        @JsonNames("size_filesystem")
        val sizeFilesystem: Long,
    )

    @Serializable
    data class Items(
        @JsonNames("snapshots")
        val items: List<Snapshot>,
    ) : Collection<Snapshot> by items
}
