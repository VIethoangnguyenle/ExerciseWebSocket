package com.hoang.webSocket.enums;

public enum MessageType {
    DERIVATIVE("D"),
    STOCK("S"),
    MARKET_INFO("MI"),
    MARKET_STAT("MS");

    String value;

    MessageType(String value) {
        this.value = value;
    }
}
