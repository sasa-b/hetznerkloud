@file:OptIn(ExperimentalSerializationApi::class)

package tech.sco.hetznerkloud.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames
import tech.sco.hetznerkloud.serialization.DatacenterIdSerializer

@Serializable
data class Datacenter(
    val id: Id,
    val description: String,
    val location: Location,
    val name: String,
    @JsonNames("server_types")
    val serverTypes: ServerTypes,
) {

    @Serializable(with = DatacenterIdSerializer::class)
    data class Id(override val value: Long) : ResourceId()

    @Serializable
    data class ServerTypes(
        val available: List<Int>,
        @JsonNames("available_for_migration")
        val availableForMigration: List<Int>,
        val supported: List<Int>,
    )
}
