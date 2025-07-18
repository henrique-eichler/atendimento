package br.com.estimular.atendimento.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Generic serializer for DTO objects.
 * Uses Jackson for JSON serialization.
 *
 * @param <T> The type of DTO to serialize
 */
public class DtoSerializer<T> implements Serializer<T> {
    private static final Logger logger = LoggerFactory.getLogger(DtoSerializer.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // Nothing to configure
    }

    @Override
    public byte[] serialize(String topic, T data) {
        if (data == null) {
            logger.debug("Null data received for serialization");
            return null;
        }

        try {
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            logger.error("Error serializing DTO: {}", e.getMessage(), e);
            throw new RuntimeException("Error serializing DTO", e);
        }
    }

    @Override
    public void close() {
        // Nothing to close
    }
}