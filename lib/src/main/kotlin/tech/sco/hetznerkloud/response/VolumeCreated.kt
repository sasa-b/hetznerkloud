@file:OptIn(ExperimentalSerializationApi::class)

package tech.sco.hetznerkloud.response

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames
import tech.sco.hetznerkloud.model.Action
import tech.sco.hetznerkloud.model.Volume

@Serializable
data class VolumeCreated(
    val action: Action,
    @JsonNames("next_actions")
    val nextActions: List<Action>,
    val volume: Volume,
)
