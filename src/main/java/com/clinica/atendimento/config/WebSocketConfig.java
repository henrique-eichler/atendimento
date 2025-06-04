package com.clinica.atendimento.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

import java.util.Map;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final com.clinica.atendimento.websocket.WebSocketHandler webSocketHandler;

    public WebSocketConfig(com.clinica.atendimento.websocket.WebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler, "/ws-cadastro")
                .setAllowedOrigins("*")
                .addInterceptors(clientUuidHandshakeInterceptor());
    }

    @Bean
    public HandshakeInterceptor clientUuidHandshakeInterceptor() {
        return new HandshakeInterceptor() {
            @Override
            public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                           WebSocketHandler wsHandler, Map<String, Object> attributes) {
                // Extract client UUID from query parameters if available
                String query = request.getURI().getQuery();
                if (query != null && query.contains("clientUuid=")) {
                    String[] params = query.split("&");
                    for (String param : params) {
                        if (param.startsWith("clientUuid=")) {
                            String clientUuid = param.substring("clientUuid=".length());
                            attributes.put("clientUuid", clientUuid);
                            break;
                        }
                    }
                }
                return true;
            }

            @Override
            public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                       WebSocketHandler wsHandler, Exception exception) {
                // Nothing to do after handshake
            }
        };
    }

    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(1024 * 1024); // 1 MB
        container.setMaxBinaryMessageBufferSize(1024 * 1024); // if using binary
        return container;
    }
}
