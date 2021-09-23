package com.hoang.webSocket.wsConfig;

import com.google.gson.Gson;
import com.hoang.webSocket.entity.StockRealtimeEntity;
import com.hoang.webSocket.wsConfig.message.MarketWebSocketMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class MarketWebSocketServer implements IMarketWebSocketServer{
    private final Gson gson= new Gson();

    @Value("#{'${market.data.interval}'.trim()}")
    int interval;

    //List session
    private final CopyOnWriteArrayList<WebSocketSession> socketSessions = new CopyOnWriteArrayList<>();

    private final List<StockRealtimeEntity> data = new CopyOnWriteArrayList<>();

    @Override
    public void addSession(WebSocketSession webSocketSession) {
        socketSessions.add(webSocketSession);
    }

    @Override
    public void removeSession(WebSocketSession webSocketSession) {
        socketSessions.remove(webSocketSession);
    }

    @Override
    public void broadcast(MarketWebSocketMessage message) {
        WebSocketMessage<String> webSocketMessage = new TextMessage(gson.toJson(message));

        // Với mỗi session tiến hành send websocket message
        socketSessions.forEach(webSocketSession -> {
            try {
                webSocketSession.sendMessage(webSocketMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
