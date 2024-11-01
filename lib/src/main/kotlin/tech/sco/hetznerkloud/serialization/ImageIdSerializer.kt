package tech.sco.hetznerkloud.serialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import tech.sco.hetznerkloud.model.Image

class ImageIdSerializer : KSerializer<Image.Id> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor(Image.Id::class.simpleName.toString(), PrimitiveKind.LONG)

    override fun serialize(encoder: Encoder, value: Image.Id) = encoder.encodeLong(value.value)

    override fun deserialize(decoder: Decoder): Image.Id = Image.Id(decoder.decodeLong())
}
