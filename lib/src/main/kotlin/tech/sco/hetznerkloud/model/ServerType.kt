@file:OptIn(ExperimentalSerializationApi::class)

package tech.sco.hetznerkloud.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames
import tech.sco.hetznerkloud.model.Server.Deprecation
import tech.sco.hetznerkloud.serialization.ServerTypeIdSerializer

@Serializable
data class ServerType(
    val id: Id,
    val architecture: String,
    val cores: Int,
    @JsonNames("cpu_type")
    val cpuType: String,
    val deprecated: Boolean,
    val deprecation: Deprecation? = null,
    val description: String,
    val disk: Int,
    val memory: Int,
    val name: String,
    val prices: List<Price>,
    @JsonNames("storage_type")
    val storageType: String,
) {
    @Serializable(with = ServerTypeIdSerializer::class)
    data class Id(override val value: Long) : ResourceId()
}
