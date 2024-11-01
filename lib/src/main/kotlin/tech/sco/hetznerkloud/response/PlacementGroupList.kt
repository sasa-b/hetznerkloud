@file:OptIn(ExperimentalSerializationApi::class)

package tech.sco.hetznerkloud.response

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames
import tech.sco.hetznerkloud.model.Meta
import tech.sco.hetznerkloud.model.PlacementGroup

@Serializable
data class PlacementGroupList(
    @JsonNames("placement_groups")
    val placementGroups: List<PlacementGroup>,
    val meta: Meta,
)
