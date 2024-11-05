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
    value class Id(val value: Long)

    @Serializable
    data class ServerTypes(
        val available: List<Int>,
        @JsonNames("available_for_migration")
        val availableForMigration: List<Int>,
        val supported: List<Int>,
    )
}
