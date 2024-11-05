package tech.sco.hetznerkloud.serialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import tech.sco.hetznerkloud.model.Volume

class VolumeIdSerializer : KSerializer<Volume.Id> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor(Volume.Id::class.simpleName.toString(), PrimitiveKind.LONG)

    override fun serialize(encoder: Encoder, value: Volume.Id) = encoder.encodeLong(value.value)

    override fun deserialize(decoder: Decoder): Volume.Id = Volume.Id(decoder.decodeLong())
}
