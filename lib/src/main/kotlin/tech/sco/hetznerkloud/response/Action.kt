@file:OptIn(ExperimentalSerializationApi::class)

package tech.sco.hetznerkloud.response

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames
import tech.sco.hetznerkloud.model.Action
import tech.sco.hetznerkloud.model.Image

@Serializable
data class ServerActionWithRootPassword(
    val action: Action,
    @JsonNames("root_password")
    val rootPassword: String? = null,
)

@Serializable
data class ServerActionWithImage(
    val action: Action,
    val image: Image,
)

@Serializable
data class ServerConsoleRequestedAction(
    val action: Action,
    val password: String,
    @JsonNames("wss_url")
    val wssUrl: String,
)
