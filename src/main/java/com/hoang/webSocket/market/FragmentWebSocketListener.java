package com.hoang.webSocket.market;

import com.neovisionaries.ws.client.WebSocket;

public interface FragmentWebSocketListener {
    void onTextMessage(WebSocket webSocket, String message);
}
