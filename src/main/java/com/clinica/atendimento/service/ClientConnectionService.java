package com.clinica.atendimento.service;

import com.clinica.atendimento.websocket.WebSocketHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ClientConnectionService {

    private final WebSocketHandler webSocketHandler;

    public ClientConnectionService(WebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    /**
     * Get information about all connected clients
     * @return List of ClientInfo objects
     */
    public List<ClientInfo> getConnectedClients() {
        Map<String, WebSocketSession> sessions = webSocketHandler.getSessions();
        
        return sessions.entrySet().stream()
                .map(entry -> {
                    String clientId = entry.getKey();
                    WebSocketSession session = entry.getValue();
                    
                    return new ClientInfo(
                            clientId,
                            session.getRemoteAddress().toString(),
                            session.isOpen(),
                            Instant.now().toString()
                    );
                })
                .collect(Collectors.toList());
    }

    /**
     * Data class to hold client connection information
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClientInfo {
        private String clientId;
        private String remoteAddress;
        private boolean connected;
        private String lastActivity;
    }
}