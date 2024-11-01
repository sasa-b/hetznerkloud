package tech.sco.hetznerkloud.serialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import tech.sco.hetznerkloud.model.Action

class ActionIdSerializer : KSerializer<Action.Id> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor(Action.Id::class.simpleName.toString(), PrimitiveKind.LONG)

    override fun serialize(encoder: Encoder, value: Action.Id) = encoder.encodeLong(value.value)

    override fun deserialize(decoder: Decoder): Action.Id = Action.Id(decoder.decodeLong())
}
