package com.clinica.atendimento.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry
import org.springframework.web.socket.server.HandshakeInterceptor
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean
import java.text.SimpleDateFormat
import java.util.Date

@Configuration
@EnableWebSocket
class WebSocketConfig(private val webSocketHandler: WebSocketHandler) : WebSocketConfigurer {

    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(webSocketHandler, "/ws-atendimento")
                .setAllowedOrigins("*")
                .addInterceptors(clientUuidHandshakeInterceptor())
    }

    @Bean
    fun clientUuidHandshakeInterceptor(): HandshakeInterceptor {
        return object : HandshakeInterceptor {
            override fun beforeHandshake(
                    request: ServerHttpRequest,
                    response: ServerHttpResponse,
                    wsHandler: WebSocketHandler,
                    attributes: MutableMap<String, Any>): Boolean {

                // Extract client UUID from query parameters if available
                val query = request.uri.query
                if (query != null && query.contains("clientUuid=")) {
                    val params = query.split("&")
                    for (param in params) {
                        if (param.startsWith("clientUuid=")) {
                            val clientUuid = param.substring("clientUuid=".length)
                            attributes["clientUuid"] = clientUuid
                            break
                        }
                    }
                }

                attributes["startedAt"] = SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Date())
                return true
            }

            override fun afterHandshake(
                    request: ServerHttpRequest,
                    response: ServerHttpResponse,
                    wsHandler: WebSocketHandler,
                    exception: Exception?) {
                // Nothing to do after handshake
            }
        }
    }

    @Bean
    fun createWebSocketContainer(): ServletServerContainerFactoryBean {
        val servletServerContainerFactoryBean = ServletServerContainerFactoryBean()
        servletServerContainerFactoryBean.maxTextMessageBufferSize = 1024 * 1024 // 1 MB
        servletServerContainerFactoryBean.maxBinaryMessageBufferSize = 1024 * 1024 // if using binary
        return servletServerContainerFactoryBean
    }
}