package com.hoang.webSocket.wsConfig;

import com.google.gson.Gson;
import com.hoang.webSocket.entity.StockRealtimeEntity;
import com.hoang.webSocket.wsConfig.message.MarketWebSocketMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@Slf4j
public class MarketWebSocketServer implements IMarketWebSocketServer{
    private final Gson gson= new Gson();

    @Value("#{'${market.data.interval}'.trim()}")
    int interval;

    //List session
    private final CopyOnWriteArrayList<WebSocketSession> socketSessions = new CopyOnWriteArrayList<>();

    private final List<StockRealtimeEntity> data = new CopyOnWriteArrayList<>();

    @Override
    public void addSession(WebSocketSession webSocketSession) {
        log.info("add ws {}", webSocketSession.getId());
        socketSessions.add(webSocketSession);
    }

    @Override
    public void removeSession(WebSocketSession webSocketSession) {
        log.info("remove ws {}", webSocketSession.getId());
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

    @Override
    public void stackData(StockRealtimeEntity data) {
        StockRealtimeEntity oldData = this.data.stream().filter(item -> item.getS().equals(data.getS())).findFirst().orElse(null);
        if (oldData == null) {
            this.data.add(data);
        }else {
            mergeData(oldData, data);
        }
    }
    @Bean
    public void autoBroadcast() {
        new Thread(() -> {
            try {
                while (true) {
                    if (!data.isEmpty()) {
                        MarketWebSocketMessage message = MarketWebSocketMessage.buildDataMessage(data);
                        broadcast(message);
                        data.clear();
                    }
                    Thread.sleep(interval);
                }
            } catch (InterruptedException interruptedException) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    private void mergeData(StockRealtimeEntity currentData, StockRealtimeEntity newData) {

        Method[] methods = StockRealtimeEntity.class.getDeclaredMethods();

        for (Method method : methods) {
            if (method.getName().startsWith("get")) {
                try {
                    Object data = method.invoke(newData);
                    if (data != null) {
                        Method setMethod = StockRealtimeEntity.class.getMethod(method.getName().replace("get", "set"), method.getReturnType());
                        setMethod.invoke(currentData, data);
                    }
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ignored) {

                }
            }
        }
    }
}
