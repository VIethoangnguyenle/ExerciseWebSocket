package com.hoang.webSocket.wsConfig.message;

import lombok.Data;

@Data
public class MarketWebSocketMessage {
    private Command cmd;

    private Object data;

    protected MarketWebSocketMessage(Command cmd, Object data) {
        this.cmd = cmd;
        this.data = data;
    }

    public static MarketWebSocketMessage buildDataMessage(Object data) {
        return new MarketWebSocketMessage(Command.D, data);
    }

    public enum Command{
        D,
        S,
        I,
    }
}
