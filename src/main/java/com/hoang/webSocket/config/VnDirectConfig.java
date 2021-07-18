package com.hoang.webSocket.config;

import com.hoang.webSocket.enums.VndCommand;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Configuration
@Slf4j
public class VnDirectConfig {

    @Autowired
    WebSocket webSocket;

    @Autowired
    SimpMessagingTemplate template;

    @EventListener(ApplicationReadyEvent.class)
    public void getVnDirectData() {
        log.info("Start calling VNDIRECT to get Market data");
        try {
            webSocket.addListener(new WebSocketAdapter() {
                @Override
                public void onTextMessage(WebSocket websocket, String message) throws Exception {
                    // Receive a text message
                    template.convertAndSend("/topic/data", message);
                    if (message.equals(VndCommand.PING.value)) {
                        websocket.sendText(VndCommand.PING.value);
                    }
                }
            });
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
        }
    }
}
