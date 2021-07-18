package com.hoang.webSocket.enums;

public enum VndCommand {
    ACKNOWLEDGE("a"),
    SUBSCRIBE("s"),
    UNSUBSCRIBE("u"),
    DATA("d"),
    PING("0"),
    PONG("1"),
    CONNECTION("c"),
    VERSION("v"),
    RESET("r");

    public String value;

    VndCommand(String value) {
        this.value = value;
    }
}
