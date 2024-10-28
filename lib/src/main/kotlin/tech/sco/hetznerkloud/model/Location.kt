@file:OptIn(ExperimentalSerializationApi::class)

package tech.sco.hetznerkloud.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
data class Location(
    val id: Int,
    val city: String,
    val country: String,
    val description: String,
    val latitude: Double,
    val longitude: Double,
    val name: String,
    @JsonNames("network_zone")
    val networkZone: String,
)
