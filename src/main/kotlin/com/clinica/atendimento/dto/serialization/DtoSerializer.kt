package com.clinica.atendimento.dto.serialization

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.common.serialization.Serializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Generic serializer for DTO objects.
 * Uses Jackson for JSON serialization.
 *
 * @param T The type of DTO to serialize
 */
class DtoSerializer<T> : Serializer<T> {
    private val objectMapper = ObjectMapper()

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(DtoSerializer::class.java)
    }

    override fun configure(configs: Map<String, *>, isKey: Boolean) {
        // Nothing to configure
    }

    override fun serialize(topic: String, data: T?): ByteArray? {
        if (data == null) {
            logger.debug("Null data received for serialization")
            return null
        }
        
        return try {
            objectMapper.writeValueAsBytes(data)
        } catch (e: Exception) {
            logger.error("Error serializing DTO: {}", e.message, e)
            throw RuntimeException("Error serializing DTO", e)
        }
    }

    override fun close() {
        // Nothing to close
    }
}