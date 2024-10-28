package tech.sco.hetznerkloud.response

import kotlinx.serialization.Serializable
import tech.sco.hetznerkloud.model.Image
import tech.sco.hetznerkloud.model.Meta

@Serializable
data class ImageList(
    val meta: Meta,
    val images: List<Image>,
)
