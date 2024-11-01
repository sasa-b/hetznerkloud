package tech.sco.hetznerkloud.serialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import tech.sco.hetznerkloud.model.PlacementGroup

class PlacementGroupIdSerializer : KSerializer<PlacementGroup.Id> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor(PlacementGroup.Id::class.simpleName.toString(), PrimitiveKind.LONG)

    override fun deserialize(decoder: Decoder): PlacementGroup.Id = PlacementGroup.Id(decoder.decodeLong())

    override fun serialize(encoder: Encoder, value: PlacementGroup.Id) = encoder.encodeLong(value.value)
}
