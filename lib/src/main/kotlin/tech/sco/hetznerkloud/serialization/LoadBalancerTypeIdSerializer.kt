package tech.sco.hetznerkloud.serialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import tech.sco.hetznerkloud.model.LoadBalancerType

class LoadBalancerTypeIdSerializer : KSerializer<LoadBalancerType.Id> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor(LoadBalancerType.Id::class.simpleName.toString(), PrimitiveKind.LONG)

    override fun serialize(encoder: Encoder, value: LoadBalancerType.Id) = encoder.encodeLong(value.value)

    override fun deserialize(decoder: Decoder): LoadBalancerType.Id = LoadBalancerType.Id(decoder.decodeLong())
}
