package com.clinica.atendimento.websocket;

import com.clinica.atendimento.repository.RecursoRepository;
import com.clinica.atendimento.repository.RecursoSalaRepository;
import com.clinica.atendimento.repository.SalaRepository;
import com.clinica.atendimento.repository.TerapiaSalaRepository;
import com.clinica.atendimento.service.AbstractServiceHandler;
import com.clinica.atendimento.service.AudioService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private final Map<String, AbstractServiceHandler<?>> services = new ConcurrentHashMap<>();

    private final AudioService audioService;
    private final ObjectMapper objectMapper;

    @Getter
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    public WebSocketHandler(RecursoRepository recursoRepository,
                            SalaRepository salaRepository,
                            TerapiaSalaRepository terapiaSalaRepository,
                            RecursoSalaRepository recursoSalaRepository,
                            AudioService audioService,
                            ObjectMapper objectMapper) {
        this.audioService = audioService;
        this.objectMapper = objectMapper;
    }

    public void register(String topic, AbstractServiceHandler<?> abstractServiceHandler) {
        services.put(topic, abstractServiceHandler);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        // Store the session with the client UUID as the key
        String clientUuid = getClientUuid(session);
        sessions.put(clientUuid, session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        JsonNode jsonNode = objectMapper.readTree(payload);

        // Extract destination and body from the message
        String destination = jsonNode.get("destination").asText();
        JsonNode body = jsonNode.has("body") ? objectMapper.readTree(jsonNode.get("body").asText()) : null;

        // Handle different message types based on destination
        switch (destination) {

            // Transcricao endpoints
            case "/app/transcricao/iniciar":
                handleIniciarTranscricao(session);
                break;
            case "/app/transcricao/chunk":
                handleChunkTranscricao(session, body);
                break;
            case "/app/transcricao/finalizar":
                handleFinalizarTranscricao(session);
                break;

            default:
                AbstractServiceHandler<?> abstractServiceHandler = services.get(destination);
                if (abstractServiceHandler != null) {
                    Class<?> clazz = abstractServiceHandler.getType();
                    Object object = jsonNode.has("body") ? objectMapper.readValue(jsonNode.get("body").asText(), clazz) : null;
                    processGeneric(abstractServiceHandler, session, object);
                }
                break;
        }
    }

    @SuppressWarnings("unchecked")
    private <T> void processGeneric(AbstractServiceHandler<T> handler, WebSocketSession session, Object obj) throws IOException {
        handler.process(session, (T) obj);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        // Remove the session when the connection is closed
        String clientUuid = getClientUuid(session);
        sessions.remove(clientUuid);
    }

    private String getClientUuid(WebSocketSession session) {
        Map<String, Object> attributes = session.getAttributes();
        return (String) attributes.getOrDefault("clientUuid", session.getId());
    }

    public void sendToSession(WebSocketSession session, String destination, Object data) throws IOException {
        // Create a message with destination and body
        Map<String, Object> message = Map.of(
                "destination", destination,
                "body", data
        );

        // Convert to JSON and send
        String jsonMessage = objectMapper.writeValueAsString(message);
        session.sendMessage(new TextMessage(jsonMessage));
    }

    // Transcricao handlers
    private void handleIniciarTranscricao(WebSocketSession session) {
        String clientId = getClientUuid(session);
        audioService.iniciarSessao(clientId);
    }

    private void handleChunkTranscricao(WebSocketSession session, JsonNode body) throws IOException {
        String clientId = getClientUuid(session);
        try {
            Chunk chunk = objectMapper.treeToValue(body, Chunk.class);
            byte[] audio = Base64.getDecoder().decode(chunk.getBase64());
            AudioService.Chunk audioChunk = new AudioService.Chunk(chunk.getIndice(), audio);
            audioService.receberChunk(clientId, audioChunk);
        } catch (IllegalArgumentException e) {
            // Handle base64 decoding error
            sendToSession(session, "/topic/transcricao/error", new Response("error", "Error decoding audio data"));
        }
    }

    private void handleFinalizarTranscricao(WebSocketSession session) {
        String clientId = getClientUuid(session);
        audioService.finalizarSessao(clientId);
    }

    // Method to be called by consumer services
    public void enviarTranscricao(String clientId, String tipo, String conteudo) throws IOException {
        Response response = new Response(tipo, conteudo);

        // Find the session for this client
        WebSocketSession session = sessions.get(clientId);
        if (session != null && session.isOpen()) {
            sendToSession(session, "/topic/transcricao/resultado", response);
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private String tipo;
        private String conteudo;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Chunk {
        private int indice;
        private String base64;
    }

    // Broadcast a message to all connected sessions
    public void broadcast(String destination, Object data) throws IOException {
        for (WebSocketSession session : sessions.values()) {
            if (session.isOpen()) {
                sendToSession(session, destination, data);
            }
        }
    }
}
