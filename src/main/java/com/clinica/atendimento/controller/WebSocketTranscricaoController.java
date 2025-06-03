package com.clinica.atendimento.controller;

import com.clinica.atendimento.websocket.WebSocketHandler;
import org.springframework.stereotype.Controller;

import java.io.IOException;

/**
 * This controller has been refactored to delegate to WebSocketHandler.
 * WebSocket functionality has been moved to com.clinica.atendimento.websocket.WebSocketHandler
 * to use native WebSocket instead of STOMP.
 * This class is maintained for backward compatibility with consumer services.
 */
@Controller
public class WebSocketTranscricaoController {

    private final WebSocketHandler webSocketHandler;

    public WebSocketTranscricaoController(WebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    /**
     * Method to be called by consumer services
     * Delegates to WebSocketHandler
     */
    public void enviar(String clientId, String tipo, String conteudo) {
        try {
            webSocketHandler.enviarTranscricao(clientId, tipo, conteudo);
        } catch (IOException e) {
            // Log error
            System.err.println("Error sending transcription result: " + e.getMessage());
        }
    }
}
