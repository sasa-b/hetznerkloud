@file:OptIn(ExperimentalSerializationApi::class)

package tech.sco.hetznerkloud.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames
import tech.sco.hetznerkloud.serialization.LocationIdSerializer

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
    val networkZone: String,
) {
    @Serializable(with = LocationIdSerializer::class)
    data class Id(override val value: Long) : ResourceId()
}
