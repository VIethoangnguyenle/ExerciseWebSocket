package com.hoang.webSocket.wsConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class MarketWebSocketServerConfig implements WebSocketConfigurer {

    @Autowired
    IMarketWebSocketServer marketWebSocketServer;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new MarketWebSocketServerHandler(marketWebSocketServer), "/market")
                .setAllowedOrigins("*");
    }
}
