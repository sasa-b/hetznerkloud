package tech.sco.hetznerkloud.serialization

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.listSerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.encodeStructure
import tech.sco.hetznerkloud.model.ServerMetrics

object TimeSeriesValuePairSerializer : KSerializer<ServerMetrics.TimeSeries.ValuePair> {
    @OptIn(ExperimentalSerializationApi::class)
    override val descriptor: SerialDescriptor =
        SerialDescriptor("ServerMetrics.TimeSeriesValuePair", listSerialDescriptor<Double>())

    override fun serialize(encoder: Encoder, value: ServerMetrics.TimeSeries.ValuePair) {
        encoder.encodeStructure(descriptor) {
            encodeDoubleElement(descriptor, 0, value.first)
            encodeStringElement(descriptor, 1, value.second)
        }
    }

    override fun deserialize(decoder: Decoder): ServerMetrics.TimeSeries.ValuePair {
        var first: Double? = null
        var second: String? = null

        val composite = decoder.beginStructure(descriptor)
        while (true) {
            when (val index = composite.decodeElementIndex(descriptor)) {
                0 -> first = composite.decodeDoubleElement(descriptor, 0)
                1 -> second = composite.decodeStringElement(descriptor, 1)
                CompositeDecoder.DECODE_DONE -> break // Input is over
                else -> error("Unexpected index: $index")
            }
        }
        composite.endStructure(descriptor)

        require(first != null && second != null) {
            "Missing one of ServerMetrics.TimeSeries.ValuePair values"
        }

        return ServerMetrics.TimeSeries.ValuePair(first, second)
    }
}
