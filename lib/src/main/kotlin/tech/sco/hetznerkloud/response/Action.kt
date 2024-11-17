@file:OptIn(ExperimentalSerializationApi::class)

package tech.sco.hetznerkloud.response

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames
import tech.sco.hetznerkloud.model.Action

@Serializable
data class ServerRootPasswordReset(
    val action: Action,
    @JsonNames("root_password")
    val rootPassword: String,
)

@Serializable
data class ServerConsoleRequested(
    val action: Action,
    val password: String,
    @JsonNames("wss_url")
    val wssUrl: String,
)
