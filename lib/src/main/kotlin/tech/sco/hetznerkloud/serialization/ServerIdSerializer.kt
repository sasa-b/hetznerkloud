package tech.sco.hetznerkloud.serialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import tech.sco.hetznerkloud.model.Server

class ServerIdSerializer : KSerializer<Server.Id> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor(Server.Id::class.simpleName.toString(), PrimitiveKind.LONG)

    override fun serialize(encoder: Encoder, value: Server.Id) = encoder.encodeLong(value.value)

    override fun deserialize(decoder: Decoder): Server.Id = Server.Id(decoder.decodeLong())
}
