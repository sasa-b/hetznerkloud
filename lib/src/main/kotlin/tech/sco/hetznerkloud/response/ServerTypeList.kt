@file:OptIn(ExperimentalSerializationApi::class)

package tech.sco.hetznerkloud.response

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames
import tech.sco.hetznerkloud.model.read.Meta
import tech.sco.hetznerkloud.model.read.ServerType

@Serializable
data class ServerTypeList(
    val meta: Meta,
    @JsonNames("server_types")
    val serverTypes: List<ServerType>,
)
