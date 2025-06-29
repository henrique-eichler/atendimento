package com.clinica.atendimento.dto.serialization

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.common.serialization.Deserializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Generic deserializer for DTO objects.
 * Uses Jackson for JSON deserialization.
 *
 * @param targetType The class of the DTO to deserialize
 * @param T The type of DTO to deserialize
 */
class DtoDeserializer<T>(private val targetType: Class<T>) : Deserializer<T> {
    private val objectMapper = ObjectMapper()

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(DtoDeserializer::class.java)
    }

    override fun configure(configs: Map<String, *>, isKey: Boolean) {
        // Nothing to configure
    }

    override fun deserialize(topic: String, data: ByteArray?): T? {
        if (data == null) {
            logger.debug("Null data received for deserialization")
            return null
        }
        
        return try {
            objectMapper.readValue(data, targetType)
        } catch (e: Exception) {
            logger.error("Error deserializing DTO: {}", e.message, e)
            throw RuntimeException("Error deserializing DTO", e)
        }
    }

    override fun close() {
        // Nothing to close
    }
}