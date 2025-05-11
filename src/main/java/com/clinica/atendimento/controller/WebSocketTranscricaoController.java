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

    /**
     * Extract client UUID from headers or fallback to session ID
     * @param headerAccessor the message headers
     * @return client UUID or session ID as fallback
     */
    private String getClientIdentifier(SimpMessageHeaderAccessor headerAccessor) {
        // Try to get the client UUID from the connection headers
        String clientUuid = null;
        if (headerAccessor.getSessionAttributes() != null) {
            clientUuid = (String) headerAccessor.getSessionAttributes().get("clientUuid");
        }

        // If client UUID is not available, fall back to session ID
        if (clientUuid == null || clientUuid.isEmpty()) {
            clientUuid = headerAccessor.getSessionId();
        }

        return clientUuid;
    }

    @MessageMapping("/transcricao/iniciar")
    public void iniciarSessao(SimpMessageHeaderAccessor headerAccessor) {
        String clientId = getClientIdentifier(headerAccessor);
        audioService.iniciarSessao(clientId);
    }

    @MessageMapping("/transcricao/chunk")
    public void receberChunk(@Payload Chunk chunk, SimpMessageHeaderAccessor headerAccessor) {
        String clientId = getClientIdentifier(headerAccessor);
        try {
            byte[] audio = Base64.getDecoder().decode(chunk.getBase64());
            AudioService.Chunk audioChunk = new AudioService.Chunk(chunk.getIndice(), audio);
            audioService.receberChunk(clientId, audioChunk);
        } catch (IllegalArgumentException e) {
            // Handle base64 decoding error
            messagingTemplate.convertAndSendToUser(clientId, "/queue/transcricao/error", new Response("error", "Error decoding audio data"));
        }
    }

    @MessageMapping("/transcricao/finalizar")
    public void finalizarSessao(SimpMessageHeaderAccessor headerAccessor) {
        String clientId = getClientIdentifier(headerAccessor);
        audioService.finalizarSessao(clientId);
    }


    // Method to be called by consumer services
    public void enviar(String clientId, String tipo, String conteudo) {
        Response response = new Response(tipo, conteudo);
        messagingTemplate.convertAndSendToUser(
                clientId,
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
