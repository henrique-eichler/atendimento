package com.clinica.atendimento.handler;

import com.clinica.atendimento.config.RedisService;
import com.clinica.atendimento.service.AudioService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
public class WebSocketTranscricaoHandler implements WebSocketHandler {

    private final Set<WebSocketSession> sessions = new CopyOnWriteArraySet<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final AudioService audioService;
    private final RedisService redisService;

    public WebSocketTranscricaoHandler(AudioService audioService, RedisService redisService) {
        this.audioService = audioService;
        this.redisService = redisService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String sessionId = Optional.ofNullable(session.getUri())
                .map(uri -> UriComponentsBuilder.fromUri(uri).build().getQueryParams().getFirst("sessionId"))
                .orElse(session.getId());

        session.getAttributes().put("sessionId", sessionId);

        audioService.iniciarSessao(sessionId);
        sessions.add(session);

        List<String> pendentes = redisService.popAllMessages(sessionId);
        if (pendentes != null) {
            for (String msg : pendentes) {
                try {
                    session.sendMessage(new TextMessage(msg));
                } catch (IOException e) {
                    redisService.pushMessage(sessionId, msg);
                }
            }
        }
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) {
        String sessionId = session.getAttributes().get("sessionId").toString();
        if (message instanceof TextMessage textMessage) {
            String payload = textMessage.getPayload();

            // Handle ping message to keep connection alive
            if ("PING".equalsIgnoreCase(payload)) {
                try {
                    session.sendMessage(new TextMessage("PONG"));
                    return;
                } catch (IOException e) {
                    System.err.println("Error sending PONG response: " + e.getMessage());
                    // Continue with normal error handling
                    handleTransportError(session, e);
                    return;
                }
            }

            if ("FIM".equalsIgnoreCase(payload)) {
                audioService.finalizarSessao(sessionId);
            } else {
                try {
                    Chunk chunk = objectMapper.readValue(payload, Chunk.class);
                    byte[] audio = Base64.getDecoder().decode(chunk.getBase64());
                    AudioService.Chunk audioChunk = new AudioService.Chunk(chunk.getIndice(), audio);
                    audioService.receberChunk(sessionId, audioChunk);
                } catch (JsonProcessingException e) {
                    System.err.println("Error processing message for session " + sessionId + ": " + e.getMessage());
                    try {
                        // Send error message to client
                        session.sendMessage(new TextMessage("{\"tipo\":\"error\",\"conteudo\":\"Error processing message\"}"));
                    } catch (IOException ioException) {
                        // If we can't even send an error message, handle as transport error
                        handleTransportError(session, ioException);
                    }
                } catch (IllegalArgumentException e) {
                    // This can happen with Base64 decoding errors
                    System.err.println("Error decoding base64 data for session " + sessionId + ": " + e.getMessage());
                    try {
                        session.sendMessage(new TextMessage("{\"tipo\":\"error\",\"conteudo\":\"Error decoding audio data\"}"));
                    } catch (IOException ioException) {
                        handleTransportError(session, ioException);
                    }
                }
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, @NonNull CloseStatus status) {
        String sessionId = session.getAttributes().get("sessionId").toString();
        audioService.finalizarSessao(sessionId);
        sessions.remove(session);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        String sessionId = session.getAttributes().get("sessionId").toString();
        System.err.println("WebSocket transport error for session " + sessionId + ": " + exception.getMessage());

        try {
            if (session.isOpen()) {
                session.close(CloseStatus.SERVER_ERROR);
            }
        } catch (IOException e) {
            System.err.println("Error closing WebSocket session after transport error: " + e.getMessage());
        } finally {
            sessions.remove(session);
        }
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    private WebSocketSession getSession(String sessao) {
        return sessions
                .stream()
                .filter(session -> session.getAttributes().get("sessionId").equals(sessao))
                .filter(WebSocketSession::isOpen).findFirst().orElse(null);
    }

    private void enviar(String sessao, Response response) {
        WebSocketSession session = null;
        try {
            final String conteudo = objectMapper.writeValueAsString(response);
            session = getSession(sessao);
            if (session != null && session.isOpen()) {
                session.sendMessage(new TextMessage(conteudo));
            } else {
                System.err.println("Cannot send message to session " + sessao + ": session is null or closed");
            }
        } catch (IOException e) {
            System.err.println("IO error sending message to session " + sessao + ": " + e.getMessage());
            if (session != null) {
                handleTransportError(session, e);
            }
        } catch (Exception exception) {
            System.err.println("Unexpected error sending message to session " + sessao + ": " + exception.getMessage());
            if (session != null) {
                handleTransportError(session, exception);
            }
        }
    }

    public void enviar(String sessao, String tipo, String texto) {
        final Response response = new Response(tipo, texto);
        enviar(sessao, response);
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
}
