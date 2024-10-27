package tech.sco.hetznerkloud.response

import kotlinx.serialization.Serializable
import tech.sco.hetznerkloud.model.read.Image
import tech.sco.hetznerkloud.model.read.Meta

@Serializable
data class ImageList(
    val meta: Meta,
    val images: List<Image>,
)
