package tech.sco.hetznerkloud.serialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import tech.sco.hetznerkloud.model.Certificate

class CertificateIdSerializer : KSerializer<Certificate.Id> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor(Certificate.Id::class.simpleName.toString(), PrimitiveKind.LONG)

    override fun deserialize(decoder: Decoder): Certificate.Id = Certificate.Id(decoder.decodeLong())

    override fun serialize(encoder: Encoder, value: Certificate.Id) = encoder.encodeLong(value.value)
}
