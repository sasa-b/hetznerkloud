@file:OptIn(ExperimentalSerializationApi::class)

package tech.sco.hetznerkloud.model.read

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
data class DataCenter(
    val id: Int,
    val description: String,
    val location: Location,
    val name: String,
    @JsonNames("server_types")
    val serverTypes: ServerTypes,
) {
    @Serializable
    data class ServerTypes(
        val available: List<Int>,
        @JsonNames("available_for_migration")
        val availableForMigration: List<Int>,
        val supported: List<Int>,
    )
}
