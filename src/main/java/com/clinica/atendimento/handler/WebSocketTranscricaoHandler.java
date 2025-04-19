package com.clinica.atendimento.handler;

import com.clinica.atendimento.service.AudioService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Base64;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
public class WebSocketTranscricaoHandler implements WebSocketHandler {

    private final AudioService audioService;
    private final Set<WebSocketSession> sessions = new CopyOnWriteArraySet<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public WebSocketTranscricaoHandler(AudioService audioService) {
        this.audioService = audioService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        audioService.iniciarSessao(session.getId());
        sessions.add(session);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) {
        String sessao = session.getId();
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
                audioService.finalizarSessao(sessao);
            } else {
                try {
                    Chunk chunk = objectMapper.readValue(payload, Chunk.class);
                    byte[] audio = Base64.getDecoder().decode(chunk.getBase64());
                    AudioService.Chunk audioChunk = new AudioService.Chunk(chunk.getIndice(), audio);
                    audioService.receberChunk(sessao, audioChunk);
                } catch (JsonProcessingException e) {
                    System.err.println("Error processing message for session " + sessao + ": " + e.getMessage());
                    e.printStackTrace();
                    try {
                        // Send error message to client
                        session.sendMessage(new TextMessage("{\"tipo\":\"error\",\"conteudo\":\"Error processing message\"}"));
                    } catch (IOException ioException) {
                        // If we can't even send an error message, handle as transport error
                        handleTransportError(session, ioException);
                    }
                } catch (IllegalArgumentException e) {
                    // This can happen with Base64 decoding errors
                    System.err.println("Error decoding base64 data for session " + sessao + ": " + e.getMessage());
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
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        audioService.finalizarSessao(session.getId());
        sessions.remove(session);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        System.err.println("WebSocket transport error for session " + session.getId() + ": " + exception.getMessage());
        exception.printStackTrace();

        try {
            if (session.isOpen()) {
                // Try to close the session gracefully
                session.close(CloseStatus.SERVER_ERROR);
            }
        } catch (IOException e) {
            System.err.println("Error closing WebSocket session after transport error: " + e.getMessage());
        } finally {
            // Make sure we clean up resources
            audioService.finalizarSessao(session.getId());
            sessions.remove(session);
        }
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    private WebSocketSession getSession(String sessao) {
        for (WebSocketSession session : sessions) {
            if (session.getId().equalsIgnoreCase(sessao) && session.isOpen()) {
                return session;
            }
        }
        return null;
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
            exception.printStackTrace();
            if (session != null) {
                handleTransportError(session, exception);
            }
        }
    }

    public void enviar(String sessao, String tipo, String texto) {
        final Response response = new Response(tipo, texto);
        enviar(sessao, response);
    }

    public static class Response {
        private String tipo;
        private String conteudo;

        public Response() {
        }

        public Response(String tipo, String conteudo) {
            this.tipo = tipo;
            this.conteudo = conteudo;
        }

        public String getTipo() {
            return tipo;
        }

        public void setTipo(String tipo) {
            this.tipo = tipo;
        }

        public String getConteudo() {
            return conteudo;
        }

        public void setConteudo(String conteudo) {
            this.conteudo = conteudo;
        }
    }

    public static class Chunk {
        private int indice;
        private String base64;

        public Chunk() {
        }

        public int getIndice() {
            return indice;
        }

        public void setIndice(int indice) {
            this.indice = indice;
        }

        public String getBase64() {
            return base64;
        }

        public void setBase64(String base64) {
            this.base64 = base64;
        }
    }
}
