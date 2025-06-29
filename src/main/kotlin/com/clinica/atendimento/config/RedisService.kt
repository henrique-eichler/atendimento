package com.clinica.atendimento.config

import io.lettuce.core.RedisClient
import io.lettuce.core.api.StatefulRedisConnection
import io.lettuce.core.api.sync.RedisCommands
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class RedisService(@Value("\${redis.url}") uri: String) {

    private val redisClient: RedisClient = RedisClient.create(uri) // Ex: redis://localhost:6379
    private val connection: StatefulRedisConnection<String, String> = redisClient.connect()
    private val syncCommands: RedisCommands<String, String> = connection.sync()

    fun pushMessage(sessionId: String, message: String) {
        syncCommands.rpush("pending:$sessionId", message)
    }

    fun popAllMessages(sessionId: String): List<String> {
        val messages = syncCommands.lrange("pending:$sessionId", 0, -1)
        syncCommands.del("pending:$sessionId")
        return messages
    }

    fun close() {
        connection.close()
        redisClient.shutdown()
    }
}