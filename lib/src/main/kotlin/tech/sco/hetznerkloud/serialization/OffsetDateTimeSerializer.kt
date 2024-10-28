package tech.sco.hetznerkloud.serialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.OffsetDateTime

object OffsetDateTimeSerializer : KSerializer<OffsetDateTime> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("OffsetDateTime", PrimitiveKind.STRING)

    override fun serialize(
        encoder: Encoder,
        value: OffsetDateTime,
    ) {
        val formatted = value.toString() // Format to ISO 8601 UTC Offset Date-Time string
        encoder.encodeString(formatted)
    }

    override fun deserialize(decoder: Decoder): OffsetDateTime {
        val dateString = decoder.decodeString()
        return OffsetDateTime.parse(dateString) // Parse ISO 8601 UTC Offset Date-Time string to OffsetDateTime
    }
}
