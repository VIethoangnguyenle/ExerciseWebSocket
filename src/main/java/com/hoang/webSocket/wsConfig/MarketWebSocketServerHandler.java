package com.hoang.webSocket.wsConfig;

import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class MarketWebSocketServerHandler extends TextWebSocketHandler {
    private final IMarketWebSocketServer marketWebSocketServer;

    public MarketWebSocketServerHandler(IMarketWebSocketServer marketWebSocketServer) {
        this.marketWebSocketServer = marketWebSocketServer;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        marketWebSocketServer.addSession(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        session.sendMessage(message);
    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        marketWebSocketServer.removeSession(session);
    }
}
