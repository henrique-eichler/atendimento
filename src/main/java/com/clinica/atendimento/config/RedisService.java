package com.clinica.atendimento.config;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RedisService {

    private final RedisClient redisClient;
    private final StatefulRedisConnection<String, String> connection;
    private final RedisCommands<String, String> syncCommands;

    public RedisService(@Value("${redis.url}") String uri) {
        this.redisClient = RedisClient.create(uri); // Ex: redis://localhost:6379
        this.connection = redisClient.connect();
        this.syncCommands = connection.sync();
    }

    public void pushMessage(String sessionId, String message) {
        syncCommands.rpush("pending:" + sessionId, message);
    }

    public List<String> popAllMessages(String sessionId) {
        List<String> messages = syncCommands.lrange("pending:" + sessionId, 0, -1);
        syncCommands.del("pending:" + sessionId);
        return messages;
    }

    public void close() {
        connection.close();
        redisClient.shutdown();
    }
}