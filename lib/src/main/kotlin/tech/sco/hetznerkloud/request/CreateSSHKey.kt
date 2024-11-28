@file:OptIn(ExperimentalSerializationApi::class)

package tech.sco.hetznerkloud.request

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import tech.sco.hetznerkloud.model.Labels

@Serializable
data class CreateSSHKey(
    val labels: Labels,
    val name: String,
    @SerialName("public_key")
    val publicKey: String,
) : HttpBody
