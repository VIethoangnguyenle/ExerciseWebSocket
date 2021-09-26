package com.hoang.webSocket.market;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoang.webSocket.entity.StockRealtimeEntity;
import com.hoang.webSocket.repository.StockRealtimeRepository;
import com.hoang.webSocket.wsConfig.IMarketWebSocketServer;
import com.hoang.webSocket.wsConfig.message.MarketWebSocketMessage;
import com.neovisionaries.ws.client.WebSocketFactory;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@EnableScheduling
@Configuration
@Slf4j
public class MSBSocketConfig {

    @Autowired
    IMarketWebSocketServer marketWebSocketServer;

    @Autowired
    StockRealtimeRepository stockRealtimeRepository;

    private final int SOCKET_FRAGMENT = 100;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final WebSocketFactory webSocketFactory = new WebSocketFactory();
    private final List<FragmentedWebSocket> fragmentedWebSockets = new ArrayList<>();


    public void getVnDirectData(List<String> stocks) {

        log.info("Start calling MBS to get Market data");

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        if (fragmentedWebSockets.isEmpty()) {
            initWebSocket(stocks);
        }

    }

    /*
        Trong initWebsocket chia danh sacsh stock thành nhiều siblist mỗi sublist 100 stockcode và ứng với 1 socket
     */
    private void initWebSocket(List<String> stock) {
        List<String> subList = new ArrayList<>();

        for (int i = 0 ; i < stock.size() ; i ++){
            if (i % SOCKET_FRAGMENT == 0 && i != 0) {
                fragmentedWebSockets.add(new FragmentedWebSocket(webSocketFactory, subList, getFragmentWebSocketListener()));
                subList.clear();
            }
            else {
                subList.add(stock.get(i));
            }
        }
    }

    private FragmentWebSocketListener getFragmentWebSocketListener() {
        return (webSocket, message) -> {
            if (message.startsWith("a")) {
                String raw = message.substring(1);
                JSONArray jsonArray = new JSONArray(raw);
                JSONObject body = new JSONObject(jsonArray.getString(0)).optJSONObject("body");
                if (body != null) {
                    if (body.has("data")) {
                        JSONObject data = body.getJSONObject("data");
                        if (data.has("sym")) {
                            String stockCode = data.getString("sym");

                            Optional<StockRealtimeEntity> stockOpt = stockRealtimeRepository.findById(stockCode);
                            StockRealtimeEntity dbStock = stockOpt.orElseGet(StockRealtimeEntity::new);
                            StockRealtimeEntity changedData = buildStockFromData(data, dbStock);

                            MarketWebSocketMessage marketWebSocketMessage = MarketWebSocketMessage.buildDataMessage(Collections.singletonList(changedData));
                            marketWebSocketServer.broadcast(marketWebSocketMessage);
                        }
                    }
                }
            }
        };
    }

    private StockRealtimeEntity buildStockFromData(JSONObject data, StockRealtimeEntity dbStock) {

        StockRealtimeEntity stock = new StockRealtimeEntity();

        stock.setS(data.getString("sym"));
        dbStock.setS(data.getString("sym"));

        Float ceil = data.optFloat("cp");
        Float floor = data.optFloat("fp");
        Float ref = data.optFloat("rp");

        Float high = data.optFloat("highestP");
        Float avg = data.optFloat("avgP");
        Float low = data.optFloat("lowestP");

        Float matchPrice = data.optFloat("mp");
        long matchVol = 0;
        long totalQty = 0;
        long foreignSellQty = 0;
        long foreignBuyQty = 0;
        long remainForeignQty = 0;

        String matchVolStr = data.optString("mv");
        String totalQtyStr = data.optString("tstraded");
        String foreignSellQtyStr = data.optString("fsr");
        String foreignBuyQtyStr = data.optString("fbr");
        remainForeignQty = data.optLong("fcr");

        if (!matchVolStr.equals("")) {
            matchVol = Long.parseLong(matchVolStr.replaceAll(",", "")) * 10;
        }

        if (!totalQtyStr.equals("")) {
            totalQty = Long.parseLong(totalQtyStr.replaceAll(",", "")) * 10;
        }

        if (!foreignSellQtyStr.equals("")) {
            foreignSellQty = Long.parseLong(foreignSellQtyStr.replaceAll(",", "")) * 10;
        }

        if (!foreignBuyQtyStr.equals("")) {
            foreignBuyQty = Long.parseLong(foreignBuyQtyStr.replaceAll(",", "")) * 10;
        }


        if (!ceil.isNaN()) {
            stock.setC(ceil);
            dbStock.setC(ceil);
        }

        if (!floor.isNaN()) {
            stock.setF(floor);
            dbStock.setF(floor);
        }

        if (!ref.isNaN()) {
            stock.setR(ref);
            dbStock.setR(ref);
        }

        if (!high.isNaN()) {
            stock.setH(high);
            dbStock.setH(high);
        }

        if (!avg.isNaN()) {
            stock.setA(avg);
            dbStock.setA(avg);
        }

        if (!low.isNaN()) {
            stock.setL(low);
            dbStock.setL(low);
        }

        if (!matchPrice.isNaN()) {
            stock.setMp(matchPrice);
            dbStock.setMp(matchPrice);
        }

        if (matchVol != 0) {
            stock.setMv(matchVol);
            dbStock.setMv(matchVol);
        }

        if (totalQty != 0) {
            stock.setTq(totalQty);
            dbStock.setTq(totalQty);
        }

        if (foreignSellQty != 0) {
            stock.setFsq(foreignSellQty);
            dbStock.setFsq(foreignSellQty);
        }

        if (foreignBuyQty != 0) {
            stock.setFbq(foreignBuyQty);
            dbStock.setFbq(foreignBuyQty);
        }

        if (remainForeignQty != 0) {
            stock.setRfq(remainForeignQty);
            dbStock.setRfq(remainForeignQty);
        }

        for (int i = 1; i <= 10; i++) {
            try {
                String buyVolStr = data.optString("bbv" + i);
                String sellVolStr = data.optString("bav" + i);
                Float buyPrice = data.optFloat("bbp" + i);
                long buyVol = 0;
                Float sellPrice = data.optFloat("bap" + i);
                long sellVol = 0;

                if (!buyVolStr.equals("")) {
                    buyVol = Long.parseLong(buyVolStr.replaceAll(",", "")) * 10;
                }

                if (!sellVolStr.equals("")) {
                    sellVol = Long.parseLong(sellVolStr.replaceAll(",", "")) * 10;
                }

                if (!buyPrice.isNaN()) {
                    stock.getClass().getMethod("setBp" + i, Float.class).invoke(stock, buyPrice);
                    dbStock.getClass().getMethod("setBp" + i, Float.class).invoke(dbStock, buyPrice);
                }

                if (buyVol != 0) {
                    stock.getClass().getMethod("setBv" + i, Long.class).invoke(stock, buyVol);
                    dbStock.getClass().getMethod("setBv" + i, Long.class).invoke(dbStock, buyVol);
                }

                if (!sellPrice.isNaN()) {
                    stock.getClass().getMethod("setSp" + i, Float.class).invoke(stock, sellPrice);
                    dbStock.getClass().getMethod("setSp" + i, Float.class).invoke(dbStock, sellPrice);
                }

                if (sellVol != 0) {
                    stock.getClass().getMethod("setSv" + i, Long.class).invoke(stock, sellVol);
                    dbStock.getClass().getMethod("setSv" + i, Long.class).invoke(dbStock, sellVol);
                }

                String buyPriceStr = data.optString("bbp" + i);
                String sellPriceStr = data.optString("bap" + i);

                if (buyPriceStr.equals("ATO")) {
                    stock.getClass().getMethod("setBp" + i, Float.class).invoke(stock, -1f);
                    dbStock.getClass().getMethod("setBp" + i, Float.class).invoke(dbStock, -1f);
                } else if (buyPriceStr.equals("ATC")) {
                    stock.getClass().getMethod("setBp" + i, Float.class).invoke(stock, -2f);
                    dbStock.getClass().getMethod("setBp" + i, Float.class).invoke(dbStock, -2f);
                }

                if (sellPriceStr.equals("ATO")) {
                    stock.getClass().getMethod("setSp" + i, Float.class).invoke(stock, -1f);
                    dbStock.getClass().getMethod("setSp" + i, Float.class).invoke(dbStock, -1f);
                } else if (sellPriceStr.equals("ATC")) {
                    stock.getClass().getMethod("setSp" + i, Float.class).invoke(stock, -2f);
                    dbStock.getClass().getMethod("setSp" + i, Float.class).invoke(dbStock, -2f);
                }

            } catch (Exception ignored) {
            }
        }

        saveStock(dbStock);
        return stock;
    }

    @Transactional
    public void saveStock(StockRealtimeEntity stockRealtimeEntity) {
        stockRealtimeRepository.save(stockRealtimeEntity);
    }

    @Scheduled(cron = "0 55 8 * * 0-6", zone = "Asia/Saigon")
    public void retryWebsockets() {
        fragmentedWebSockets.forEach(FragmentedWebSocket::retryConnect);
    }

    @Scheduled(fixedDelay = 5000)
    public void pingInterval() {
        fragmentedWebSockets.forEach(fragmentedWebSocket -> {
            fragmentedWebSocket.sendText("[\"{\\\"type\\\":\\\"ping\\\"}\"]");
        });
    }
}

