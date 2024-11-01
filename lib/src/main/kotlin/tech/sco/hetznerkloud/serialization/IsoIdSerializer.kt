package tech.sco.hetznerkloud.serialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import tech.sco.hetznerkloud.model.Iso

class IsoIdSerializer : KSerializer<Iso.Id> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor(Iso.Id::class.simpleName.toString(), PrimitiveKind.LONG)

    override fun deserialize(decoder: Decoder): Iso.Id = Iso.Id(decoder.decodeLong())

    override fun serialize(encoder: Encoder, value: Iso.Id) = encoder.encodeLong(value.value)
}
