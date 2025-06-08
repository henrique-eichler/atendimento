package com.clinica.atendimento.service;

import com.clinica.atendimento.service.websocket.WebSocketHandler;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class SessionListServiceHandler extends AbstractServiceHandler<Void> {

    private final WebSocketHandler webSocketHandler;

    public SessionListServiceHandler(WebSocketHandler webSocketHandler) {
        super(Void.class);
        this.webSocketHandler = webSocketHandler;

        this.webSocketHandler.register("/topic/sessions/request/list", this);
    }

    @Override
    public void process(WebSocketSession session, Void unused) throws IOException {
        Map<String, WebSocketSession> sessions = webSocketHandler.getSessions();

        List<ClientInfo> list = sessions.values()
                .stream()
                .map(webSocketSession -> ClientInfo.builder()
                        .clientId(toString(webSocketSession.getAttributes().get("clientUuid")))
                        .connected(webSocketSession.isOpen())
                        .startedAt(toString(webSocketSession.getAttributes().get("startedAt")))
                        .updatedAt(toString(webSocketSession.getAttributes().get("updatedAt")))
                        .disconectedAt(toString(webSocketSession.getAttributes().get("disconectedAt")))
                        .build())
                .toList();
        this.webSocketHandler.sendToSession(session, "/topic/sessions/response/list", list);
    }

    private static String toString(Object object) {
        return object == null ? "" : object.toString();
    }

    /**
     * Data class to hold client connection information
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(fluent = true)
    @Builder
    public static class ClientInfo {
        @JsonProperty private String clientId;
        @JsonProperty private boolean connected;
        @JsonProperty private String startedAt;
        @JsonProperty private String updatedAt;
        @JsonProperty private String disconectedAt;
    }
}