@file:OptIn(ExperimentalSerializationApi::class)

package tech.sco.hetznerkloud.model.read

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
data class Image(
    val id: Int,
    val architecture: String,
    @JsonNames("bound_to")
    val boundTo: String? = null,
    val created: String,
    @JsonNames("created_from")
    val createdFrom: CreatedFrom,
    val deleted: String? = null,
    val deprecated: String,
    val description: String,
    @JsonNames("disk_size")
    val diskSize: Int,
    @JsonNames("image_size")
    val imageSize: Double,
    val labels: Labels = emptyMap(),
    val name: String,
    @JsonNames("os_flavor")
    val osFlavor: String,
    @JsonNames("os_version")
    val osVersion: String,
    val protection: Protection,
    @JsonNames("rapid_deploy")
    val rapidDeploy: Boolean,
    val status: String,
    val type: String,
) {
    @Serializable
    data class CreatedFrom(
        val id: Int,
        val name: String,
    )

    @Serializable
    data class Protection(
        val delete: Boolean,
    )
}
