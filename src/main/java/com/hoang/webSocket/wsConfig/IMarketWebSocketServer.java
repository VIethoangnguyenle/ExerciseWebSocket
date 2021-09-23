package com.hoang.webSocket.wsConfig;

import com.hoang.webSocket.wsConfig.message.MarketWebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

public interface IMarketWebSocketServer {
    void addSession(WebSocketSession webSocketSession);
    void removeSession(WebSocketSession webSocketSession);
    void broadcast(MarketWebSocketMessage message);
}
