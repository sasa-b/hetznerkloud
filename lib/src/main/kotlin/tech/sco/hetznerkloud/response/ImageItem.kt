package tech.sco.hetznerkloud.response

import kotlinx.serialization.Serializable
import tech.sco.hetznerkloud.model.Image

@Serializable
data class ImageItem(val image: Image)
