package com.clinica.atendimento.config;

import com.clinica.atendimento.handler.WebSocketTranscricaoHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketConfigurer, WebSocketMessageBrokerConfigurer {

    private final WebSocketTranscricaoHandler webSocketTranscricaoHandler;

    public WebSocketConfig(WebSocketTranscricaoHandler webSocketTranscricaoHandler) {
        this.webSocketTranscricaoHandler = webSocketTranscricaoHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketTranscricaoHandler, "/ws-transcricao")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/queue");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-cadastro")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }
}