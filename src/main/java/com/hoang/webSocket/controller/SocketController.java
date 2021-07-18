package com.hoang.webSocket.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class SocketController {

    @MessageMapping("/data")
    @SendTo("/topic/data")
    public String getData(String str) {
        log.info("Receive message " + str);
        return "{\"data\": " + str + "}";
    }
}
