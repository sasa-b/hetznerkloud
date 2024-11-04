package tech.sco.hetznerkloud.serialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import tech.sco.hetznerkloud.model.SSHKey

class SSHKeyIdSerializer : KSerializer<SSHKey.Id> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor(SSHKey.Id::class.simpleName.toString(), PrimitiveKind.LONG)

    override fun serialize(encoder: Encoder, value: SSHKey.Id) = encoder.encodeLong(value.value)

    override fun deserialize(decoder: Decoder): SSHKey.Id = SSHKey.Id(decoder.decodeLong())
}
