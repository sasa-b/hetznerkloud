@file:OptIn(ExperimentalSerializationApi::class)

package tech.sco.hetznerkloud.response

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames
import tech.sco.hetznerkloud.model.Action
import tech.sco.hetznerkloud.model.Server

@Serializable
data class ServerCreated(
    val action: Action,
    @JsonNames("next_actions")
    val nextActions: List<Action>,
    @JsonNames("root_password")
    val rootPassword: String?,
    val server: Server,
)
