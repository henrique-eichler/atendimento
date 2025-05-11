package com.clinica.atendimento.controller;

import com.clinica.atendimento.service.AudioService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Base64;

@Controller
public class WebSocketTranscricaoController {

    private final AudioService audioService;
    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketTranscricaoController(AudioService audioService, SimpMessagingTemplate messagingTemplate) {
        this.audioService = audioService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/transcricao/iniciar")
    public void iniciarSessao(SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        audioService.iniciarSessao(sessionId);
    }

    @MessageMapping("/transcricao/chunk")
    public void receberChunk(@Payload Chunk chunk, SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        try {
            byte[] audio = Base64.getDecoder().decode(chunk.getBase64());
            AudioService.Chunk audioChunk = new AudioService.Chunk(chunk.getIndice(), audio);
            audioService.receberChunk(sessionId, audioChunk);
        } catch (IllegalArgumentException e) {
            // Handle base64 decoding error
            messagingTemplate.convertAndSendToUser(
                    sessionId,
                    "/queue/transcricao/error",
                    new Response("error", "Error decoding audio data")
            );
        }
    }

    @MessageMapping("/transcricao/finalizar")
    public void finalizarSessao(SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        audioService.finalizarSessao(sessionId);
    }

    @MessageMapping("/transcricao/ping")
    public void ping(SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        messagingTemplate.convertAndSendToUser(
                sessionId,
                "/queue/transcricao/pong",
                "PONG"
        );
    }

    // Method to be called by consumer services
    public void enviar(String sessao, String tipo, String conteudo) {
        Response response = new Response(tipo, conteudo);
        messagingTemplate.convertAndSendToUser(
                sessao,
                "/queue/transcricao/resultado",
                response
        );
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