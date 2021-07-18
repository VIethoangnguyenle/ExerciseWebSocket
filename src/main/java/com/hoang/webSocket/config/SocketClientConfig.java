package com.hoang.webSocket.config;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Configuration
@Slf4j
public class SocketClientConfig {
    private final WebSocketFactory factory = new WebSocketFactory();

    @Value("${vn-direct.ws.url}")
    private String wsUrl;

    @Bean
    WebSocket webSocket() {
        WebSocket ws = null;
        try{
            ws = factory.createSocket(wsUrl, 5000);
            ws.addListener(webSocketAdapter());
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
        }
        return ws;
    }

    @Bean
    WebSocketAdapter webSocketAdapter() {
        return new WebSocketAdapter() {
            @Override
            public void onTextMessage(WebSocket websocket, String text) throws Exception {

            }

            @Override
            public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
                log.info("CONNECTED");
                super.onConnected(websocket, headers);
            }

            @Override
            public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
                log.info("DISCONNECTED");
                super.onDisconnected(websocket, serverCloseFrame, clientCloseFrame, closedByServer);
            }

            @Override
            public void onSendingFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {
                super.onSendingFrame(websocket, frame);
            }
        };
    }
}
