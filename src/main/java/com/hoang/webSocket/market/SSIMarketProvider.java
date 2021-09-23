package com.hoang.webSocket.market;

import com.hoang.webSocket.entity.StockRealtimeEntity;
import com.hoang.webSocket.repository.StockRealtimeRepository;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class SSIMarketProvider {

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    StockRealtimeRepository stockRealtimeRepository;
    @Autowired
    MSBSocketConfig mbsSocketConfig;

    private final List<String> stockCodes = new ArrayList<>();

    @Bean
    public void startProvider(){
        syncMarketBoard();
        mbsSocketConfig.getVnDirectData(stockCodes);
    }

    @Scheduled(cron = "0 */15 * * * *", zone = "Asia/Saigon")
    public void syncMarketBoard() {
        try {
            JSONObject request = new JSONObject();

            String fieldsQuery = buildFieldQuery();
            StringBuilder query = new StringBuilder();

            query.append("{");
            query.append("hose: stockRealtimes(exchange: \"hose\")").append(fieldsQuery);
            query.append("hnx: stockRealtimes(exchange: \"hnx\")").append(fieldsQuery);
            query.append("upcom: stockRealtimes(exchange: \"upcom\")").append(fieldsQuery);
            query.append("}");
            request.put("query", query.toString());

            String raw = restTemplate.postForObject(
                    "https://gateway-iboard.ssi.com.vn/graphql",
                    request.toMap(),
                    String.class);

            JSONObject response = new JSONObject(raw);
            if (response.has("data")) {
                JSONObject data = response.getJSONObject("data");
                if (data.has("hose")) {
                    saveSyncExchange(data.getJSONArray("hose"));
                }

                if (data.has("hnx")) {
                    saveSyncExchange(data.getJSONArray("hnx"));
                }

                if (data.has("upcom")) {
                    saveSyncExchange(data.getJSONArray("upcom"));
                }
            }
        } catch (Exception ignored){

        }
    }

    private String buildFieldQuery() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        stringBuilder.append("ceiling").append(" ");
        stringBuilder.append("floor").append(" ");
        stringBuilder.append("refPrice").append(" ");
        stringBuilder.append("stockSymbol").append(" ");
        stringBuilder.append("stockNo").append(" ");
        stringBuilder.append("stockType").append(" ");
        stringBuilder.append("exchange").append(" ");
        stringBuilder.append("matchedPrice").append(" ");
        stringBuilder.append("matchedVolume").append(" ");
        stringBuilder.append("highest").append(" ");
        stringBuilder.append("avgPrice").append(" ");
        stringBuilder.append("lowest").append(" ");
        stringBuilder.append("nmTotalTradedQty").append(" ");
        stringBuilder.append("best1Bid").append(" ");
        stringBuilder.append("best2Bid").append(" ");
        stringBuilder.append("best3Bid").append(" ");
        stringBuilder.append("best4Bid").append(" ");
        stringBuilder.append("best5Bid").append(" ");
        stringBuilder.append("best6Bid").append(" ");
        stringBuilder.append("best7Bid").append(" ");
        stringBuilder.append("best8Bid").append(" ");
        stringBuilder.append("best9Bid").append(" ");
        stringBuilder.append("best10Bid").append(" ");
        stringBuilder.append("best1BidVol").append(" ");
        stringBuilder.append("best2BidVol").append(" ");
        stringBuilder.append("best3BidVol").append(" ");
        stringBuilder.append("best4BidVol").append(" ");
        stringBuilder.append("best5BidVol").append(" ");
        stringBuilder.append("best6BidVol").append(" ");
        stringBuilder.append("best7BidVol").append(" ");
        stringBuilder.append("best8BidVol").append(" ");
        stringBuilder.append("best9BidVol").append(" ");
        stringBuilder.append("best10BidVol").append(" ");
        stringBuilder.append("best1Offer").append(" ");
        stringBuilder.append("best2Offer").append(" ");
        stringBuilder.append("best3Offer").append(" ");
        stringBuilder.append("best4Offer").append(" ");
        stringBuilder.append("best5Offer").append(" ");
        stringBuilder.append("best6Offer").append(" ");
        stringBuilder.append("best7Offer").append(" ");
        stringBuilder.append("best8Offer").append(" ");
        stringBuilder.append("best9Offer").append(" ");
        stringBuilder.append("best10Offer").append(" ");
        stringBuilder.append("best1OfferVol").append(" ");
        stringBuilder.append("best2OfferVol").append(" ");
        stringBuilder.append("best3OfferVol").append(" ");
        stringBuilder.append("best4OfferVol").append(" ");
        stringBuilder.append("best5OfferVol").append(" ");
        stringBuilder.append("best6OfferVol").append(" ");
        stringBuilder.append("best7OfferVol").append(" ");
        stringBuilder.append("best8OfferVol").append(" ");
        stringBuilder.append("best9OfferVol").append(" ");
        stringBuilder.append("best10OfferVol").append(" ");
        stringBuilder.append("currentBidQty").append(" ");
        stringBuilder.append("currentOfferQty").append(" ");
        stringBuilder.append("buyForeignQtty").append(" ");
        stringBuilder.append("buyForeignValue").append(" ");
        stringBuilder.append("sellForeignQtty").append(" ");
        stringBuilder.append("sellForeignValue").append(" ");
        stringBuilder.append("remainForeignQtty").append(" ");

        stringBuilder.append("}");

        return stringBuilder.toString();
    }

    private void saveSyncExchange(JSONArray stockList) {

        List<StockRealtimeEntity> stockRealtimeEntityList = new ArrayList<>();

        for (int i = 0; i < stockList.length(); i++) {
            JSONObject stock = stockList.getJSONObject(i);
            stockCodes.add(stock.getString("stockSymbol"));

            StockRealtimeEntity stockRealtimeEntity = new StockRealtimeEntity();
            stockRealtimeEntity.setS(stock.getString("stockSymbol"));
            stockRealtimeEntity.setNo(stock.getString("stockNo"));
            stockRealtimeEntity.setT(0);
            stockRealtimeEntity.setE(stock.getString("exchange"));

            stockRealtimeEntity.setC(stock.optFloat("ceiling", 0) / 1000);
            stockRealtimeEntity.setF(stock.optFloat("floor", 0) / 1000);
            stockRealtimeEntity.setR(stock.optFloat("refPrice", 0) / 1000);

            stockRealtimeEntity.setMp(stock.optFloat("matchedPrice", 0) / 1000);
            stockRealtimeEntity.setMv(stock.optLong("matchedVolume", 0) * 10);
            stockRealtimeEntity.setH(stock.optFloat("highest", 0) / 1000);
            stockRealtimeEntity.setA(stock.optFloat("avgPrice", 0) / 1000);
            stockRealtimeEntity.setL(stock.optFloat("lowest", 0) / 1000);
            stockRealtimeEntity.setTq(stock.optLong("nmTotalTradedQty", 0));
            stockRealtimeEntity.setBq(stock.optLong("currentBidQty", 0));
            stockRealtimeEntity.setSq(stock.optLong("currentOfferQty", 0));
            stockRealtimeEntity.setFbq(stock.optLong("buyForeignQtty", 0));
            stockRealtimeEntity.setFsq(stock.optLong("sellForeignQtty", 0));
            stockRealtimeEntity.setRfq(stock.optLong("remainForeignQtty", 0));

            for (int count = 1; count <= 10; count++) {
                try {
                    stockRealtimeEntity.getClass().getMethod("setBp" + count, Float.class).invoke(stockRealtimeEntity, stock.optFloat("best" + count + "Bid", 0) / 1000);
                    stockRealtimeEntity.getClass().getMethod("setBv" + count, Long.class).invoke(stockRealtimeEntity, stock.optLong("best" + count + "BidVol", 0));
                    stockRealtimeEntity.getClass().getMethod("setSp" + count, Float.class).invoke(stockRealtimeEntity, stock.optFloat("best" + count + "Offer", 0) / 1000);
                    stockRealtimeEntity.getClass().getMethod("setSv" + count, Long.class).invoke(stockRealtimeEntity, stock.optLong("best" + count + "OfferVol", 0));

                    if (stock.optString("best" + count + "Bid").equals("ATO")) {
                        stockRealtimeEntity.getClass().getMethod("setBp" + count, Float.class).invoke(stockRealtimeEntity, -1f);
                    } else if (stock.optString("best" + count + "Bid").equals("ATC")) {
                        stockRealtimeEntity.getClass().getMethod("setBp" + count, Float.class).invoke(stockRealtimeEntity, -2f);
                    }

                    if (stock.optString("best" + count + "Offer").equals("ATO")) {
                        stockRealtimeEntity.getClass().getMethod("setSp" + count, Float.class).invoke(stockRealtimeEntity, -1f);
                    } else if (stock.optString("best" + count + "Offer").equals("ATC")) {
                        stockRealtimeEntity.getClass().getMethod("setSp" + count, Float.class).invoke(stockRealtimeEntity, -2f);
                    }
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    log.error(e.getLocalizedMessage());
                }
            }

            stockRealtimeEntityList.add(stockRealtimeEntity);
        }

        stockRealtimeRepository.saveAll(stockRealtimeEntityList);
    }
}
