@file:OptIn(ExperimentalSerializationApi::class)

package tech.sco.hetznerkloud.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
data class Location(
    val id: Id,
    val city: String,
    val country: String,
    val description: String,
    val latitude: Double,
    val longitude: Double,
    val name: String,
    @JsonNames("network_zone")
    val networkZone: NetworkZone,
) {
    @Serializable
    @JvmInline
    value class Id(override val value: Long) : ResourceId {
        override val type: ResourceType
            get() = ResourceType.LOCATION
    }
}
