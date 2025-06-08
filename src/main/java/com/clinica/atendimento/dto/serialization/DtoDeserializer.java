package com.clinica.atendimento.dto.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Generic deserializer for DTO objects.
 * Uses Jackson for JSON deserialization.
 *
 * @param <T> The type of DTO to deserialize
 */
public class DtoDeserializer<T> implements Deserializer<T> {
    private static final Logger logger = LoggerFactory.getLogger(DtoDeserializer.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Class<T> targetType;

    /**
     * Constructor that takes the target DTO class type.
     *
     * @param targetType The class of the DTO to deserialize
     */
    public DtoDeserializer(Class<T> targetType) {
        this.targetType = targetType;
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // Nothing to configure
    }

    @Override
    public T deserialize(String topic, byte[] data) {
        if (data == null) {
            logger.debug("Null data received for deserialization");
            return null;
        }
        
        try {
            return objectMapper.readValue(data, targetType);
        } catch (Exception e) {
            logger.error("Error deserializing DTO: {}", e.getMessage(), e);
            throw new RuntimeException("Error deserializing DTO", e);
        }
    }

    @Override
    public void close() {
        // Nothing to close
    }
}