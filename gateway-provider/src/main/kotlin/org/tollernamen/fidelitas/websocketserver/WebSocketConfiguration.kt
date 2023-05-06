package org.tollernamen.fidelitas.websocketserver

import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry

val chatHandler = ChatHandler()

@Configuration
@EnableWebSocket
class WebSocketConfiguration : WebSocketConfigurer
{
    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry)
    {
        registry.addHandler(chatHandler, "/fidelitas/gate")
            .setAllowedOrigins("*")
    }
}