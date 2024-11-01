package tech.sco.hetznerkloud.serialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import tech.sco.hetznerkloud.model.Network

class NetworkIdSerializer : KSerializer<Network.Id> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor(Network.Id::class.simpleName.toString(), PrimitiveKind.LONG)

    override fun deserialize(decoder: Decoder): Network.Id = Network.Id(decoder.decodeLong())

    override fun serialize(encoder: Encoder, value: Network.Id) = encoder.encodeLong(value.value)
}
