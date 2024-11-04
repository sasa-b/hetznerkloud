@file:OptIn(ExperimentalSerializationApi::class)

package tech.sco.hetznerkloud.request

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames
import tech.sco.hetznerkloud.model.Labels

@Serializable
data class CreateSSHKey(
    val labels: Labels,
    val name: String,
    @JsonNames("public_key")
    val publicKey: String,
) : HttpBody

@Serializable
data class UpdateSSHKey(
    val labels: Labels,
    val name: String,
) : HttpBody
