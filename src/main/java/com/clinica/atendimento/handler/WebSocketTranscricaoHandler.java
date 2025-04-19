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
            if ("FIM".equalsIgnoreCase(textMessage.getPayload())) {
                audioService.finalizarSessao(sessao);
            } else {
                String payload = textMessage.getPayload();

                try {
                    Chunk chunk = objectMapper.readValue(payload, Chunk.class);
                    byte[] audio = Base64.getDecoder().decode(chunk.getBase64());
                    AudioService.Chunk audioChunk = new AudioService.Chunk(chunk.getIndice(), audio);
                    audioService.receberChunk(sessao, audioChunk);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
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
        System.out.println("handleTransportError: " + exception.getMessage());
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    private Optional<WebSocketSession> getSession(String sessao) {
        for (WebSocketSession session : sessions) {
            if (session.getId().equalsIgnoreCase(sessao) && session.isOpen()) {
                return Optional.of(session);
            }
        }
        return Optional.empty();
    }

    public void enviarTranscricao(String sessao, String transcricao) {
        try {
            final Response response = new Response("transcricao", transcricao);
            final String conteudo = objectMapper.writeValueAsString(response);
            getSession(sessao).ifPresent(session -> {
                try {
                    session.sendMessage(new TextMessage(conteudo));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void enviarResumo(String sessao, String resumo) {
        try {
            final Response response = new Response("resumo", resumo);
            final String conteudo = objectMapper.writeValueAsString(response);
            getSession(sessao).ifPresent(session -> {
                try {
                    session.sendMessage(new TextMessage(conteudo));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
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