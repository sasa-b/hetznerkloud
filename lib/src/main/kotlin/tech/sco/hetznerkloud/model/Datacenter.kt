@file:OptIn(ExperimentalSerializationApi::class)

package tech.sco.hetznerkloud.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
data class Datacenter(
    val id: Id,
    val description: String,
    val location: Location,
    val name: String,
    @JsonNames("server_types")
    val serverTypes: ServerTypes,
) {

    @Serializable
    @JvmInline
    value class Id(override val value: Long) : ResourceId {
        override val type: ResourceType
            get() = ResourceType.DATACENTER
    }

    @Serializable
    data class ServerTypes(
        val available: List<ServerType.Id>,
        @JsonNames("available_for_migration")
        val availableForMigration: List<ServerType.Id>,
        val supported: List<ServerType.Id>,
    )

    @Serializable
    data class Items(
        val meta: Meta,
        @JsonNames("datacenters")
        val items: List<Datacenter>,
        val recommendation: Long,
    ) : Collection<Datacenter> by items
}
