package mx.unam.fciencias.ids.eq1.utils

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.math.BigDecimal

/**
 * Serializer for [BigDecimal] that encodes and decodes values as strings.
 */
object BigDecimalSerializer : KSerializer<BigDecimal> {
    /**
     * Describes the serialized form of [BigDecimal] as a primitive string.
     */
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("BigDecimal", PrimitiveKind.STRING)

    /**
     * Serializes a [BigDecimal] value into its plain string representation.
     *
     * @param encoder Encoder to write the serialized value.
     * @param value The [BigDecimal] value to serialize.
     */
    override fun serialize(encoder: Encoder, value: BigDecimal) {
        encoder.encodeString(value.toPlainString())
    }

    /**
     * Deserializes a string into a [BigDecimal] value.
     *
     * @param decoder Decoder to read the serialized value.
     * @return The deserialized [BigDecimal] value.
     */
    override fun deserialize(decoder: Decoder): BigDecimal {
        return BigDecimal(decoder.decodeString())
    }
}