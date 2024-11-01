package tech.sco.hetznerkloud.serialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import tech.sco.hetznerkloud.model.ServerType

class ServerTypeIdSerializer : KSerializer<ServerType.Id> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor(ServerType.Id::class.simpleName.toString(), PrimitiveKind.LONG)

    override fun deserialize(decoder: Decoder): ServerType.Id = ServerType.Id(decoder.decodeLong())

    override fun serialize(encoder: Encoder, value: ServerType.Id) = encoder.encodeLong(value.value)
}
