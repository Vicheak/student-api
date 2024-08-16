package com.acledabank.vicheak.api.core.config;

import com.acledabank.vicheak.api.core.socket.SocketConnectionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    //register the socket
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        //expose the endpoint and manage the CORS policy
        registry.addHandler(new SocketConnectionHandler(), "/api/v1/chat")
                .setAllowedOrigins("*");
    }

}
