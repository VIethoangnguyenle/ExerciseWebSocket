package com.hoang.webSocket.config;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;

@EnableScheduling
@Configuration
@Slf4j
public class MSBSocketConfig {

    @Autowired
    SimpMessagingTemplate template;

    @Autowired
    WebSocketAdapter webSocketAdapter;

    private WebSocket webSocket = null;

    @EventListener(ApplicationReadyEvent.class)
    public void getVNDirectData() {
        log.info("Start calling MBS to get Market data");
        long tempTime = System.currentTimeMillis();
        try {
            webSocket = new WebSocketFactory().createSocket("wss://mktrlt1.mbs.com.vn/pbRltMarkets/095/k4dudemm/websocket");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
