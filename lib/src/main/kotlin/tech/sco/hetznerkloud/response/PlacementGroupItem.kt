@file:OptIn(ExperimentalSerializationApi::class)

package tech.sco.hetznerkloud.response

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames
import tech.sco.hetznerkloud.model.PlacementGroup

@Serializable
data class PlacementGroupItem(
    @JsonNames("placement_group")
    val placementGroup: PlacementGroup,
)
