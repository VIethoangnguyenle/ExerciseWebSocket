package com.hoang.webSocket.market;

import com.neovisionaries.ws.client.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FragmentedWebSocket {
    private final List<String> stockList = new ArrayList<>();
    private final WebSocketFactory webSocketFactory ;
    private final FragmentWebSocketListener listener;
    private WebSocket webSocket;

    public FragmentedWebSocket(WebSocketFactory webSocketFactory, List<String> stockList, FragmentWebSocketListener listener) {
        this.webSocketFactory = webSocketFactory;
        this.listener = listener;
        this.stockList.addAll(stockList);
        connect();
    }

    public void retryConnect() {
        if (webSocket.getSocket().isClosed() || !webSocket.isOpen()) {
            connect();
        }
    }

    public void sendText(String text) {
        if (webSocket != null && webSocket.isOpen()) {
            webSocket.sendText(text);
        }
    }

    private WebSocketAdapter getWebSocketAdapter() {
        return new WebSocketAdapter() {
            @Override
            public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
                websocket.getSocket().setKeepAlive(true);
                websocket.getSocket().setTcpNoDelay(true);
                websocket.getSocket().setPerformancePreferences(0, 1, 2);
            }

            @Override
            public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame,
                                       WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
                connect();
            }

            @Override
            public void onTextMessage(WebSocket websocket, String text) throws Exception {
                listener.onTextMessage(websocket, text);
            }
        };
    }

    // TRy to connect mbs wss
    private void connect(){
        try {
            webSocket = webSocketFactory.createSocket("wss://mktrlt1.mbs.com.vn/pbRltMarkets/095/k4dudemm/websocket");
            webSocket.addListener(getWebSocketAdapter());
            webSocket.setPingInterval(3000);
            webSocket.setAutoFlush(true);
            webSocket.setPongInterval(3000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void requestFloorData(List<String> stocks) {
        stocks.forEach(stockCode -> {
            webSocket.sendText("[\"{\\\"type\\\":\\\"register\\\",\\\"address\\\":\\\"" + stockCode + "\\\",\\\"headers\\\":{}}\"]");
        });
    }
}
