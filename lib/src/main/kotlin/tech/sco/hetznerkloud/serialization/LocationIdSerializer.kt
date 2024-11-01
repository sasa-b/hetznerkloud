package tech.sco.hetznerkloud.serialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import tech.sco.hetznerkloud.model.Location

class LocationIdSerializer : KSerializer<Location.Id> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor(Location.Id::class.simpleName.toString(), PrimitiveKind.LONG)

    override fun deserialize(decoder: Decoder): Location.Id = Location.Id(decoder.decodeLong())

    override fun serialize(encoder: Encoder, value: Location.Id) = encoder.encodeLong(value.value)
}
