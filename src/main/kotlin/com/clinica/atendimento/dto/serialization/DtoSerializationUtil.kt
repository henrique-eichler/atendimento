package com.clinica.atendimento.dto.serialization

import org.apache.kafka.common.serialization.Deserializer
import org.apache.kafka.common.serialization.Serializer

/**
 * Utility class for creating serializers and deserializers for DTO objects.
 */
object DtoSerializationUtil {

    /**
     * Creates a serializer for the specified DTO type.
     *
     * @param T The type of DTO to serialize
     * @return A serializer for the specified DTO type
     */
    @JvmStatic
    fun <T> createSerializer(): Serializer<T> {
        return DtoSerializer()
    }

    /**
     * Creates a deserializer for the specified DTO type.
     *
     * @param T The type of DTO to deserialize
     * @param targetType The class of the DTO to deserialize
     * @return A deserializer for the specified DTO type
     */
    @JvmStatic
    fun <T> createDeserializer(targetType: Class<T>): Deserializer<T> {
        return DtoDeserializer(targetType)
    }
}