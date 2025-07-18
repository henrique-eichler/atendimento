package br.com.estimular.atendimento.serialization;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;

/**
 * Utility class for creating serializers and deserializers for DTO objects.
 */
public class DtoSerializationUtil {

    /**
     * Creates a serializer for the specified DTO type.
     *
     * @param <T> The type of DTO to serialize
     * @return A serializer for the specified DTO type
     */
    public static <T> Serializer<T> createSerializer() {
        return new DtoSerializer<>();
    }

    /**
     * Creates a deserializer for the specified DTO type.
     *
     * @param <T>        The type of DTO to deserialize
     * @param targetType The class of the DTO to deserialize
     * @return A deserializer for the specified DTO type
     */
    public static <T> Deserializer<T> createDeserializer(Class<T> targetType) {
        return new DtoDeserializer<>(targetType);
    }
}