package com.hoang.webSocket.controller;

import com.hoang.webSocket.market.SSIMarketProvider;
import com.hoang.webSocket.dto.RestResponseDto;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("api/market")
public class DataController {

    RestTemplate restTemplate = new RestTemplate();

    @Autowired
    SSIMarketProvider ssiMarketProvider;

    @GetMapping("info")
    public RestResponseDto<Object> getCodeInfo() {

        String data = restTemplate.getForObject("https://api-finfo.vndirect.com.vn/v4/stocks?q=type:IFC,ETF,STOCK~status:LISTED&size=10000",
                String.class);
        String data2 = restTemplate.getForObject("https://iboard.ssi.com.vn/dchart/api/symbols?symbol=ACB", String.class);
        JSONObject response = new JSONObject(data2);
        log.info("{}", response);
        return new RestResponseDto<>().success(response.toMap());
    }

    @GetMapping("table/{table}")
    public RestResponseDto<Object> getTableData(@PathVariable String table) {

        JSONObject variables = new JSONObject();
        JSONObject request = new JSONObject();
        variables.put("group", table);
        request.put("operationName", "stockRealtimesByGroup");
        request.put("variables", variables);
        request.put("query", "query stockRealtimesByGroup($group: String) " +
                "{  stockRealtimesByGroup(group: $group) {    stockNo    ceiling    floor    refPrice    stockSymbol    " +
                "stockType    exchange    matchedPrice    matchedVolume    priceChange    priceChangePercent    highest    " +
                "avgPrice    lowest    nmTotalTradedQty    best1Bid    best1BidVol    best2Bid    best2BidVol    best3Bid    " +
                "best3BidVol    best1Offer    best1OfferVol    best2Offer    best2OfferVol    best3Offer    best3OfferVol}}");

        String data = restTemplate.postForObject(
                "https://gateway-iboard.ssi.com.vn/graphql",
                request.toMap(),
                String.class);

        List<Object> response = new JSONObject(data).getJSONObject("data").getJSONArray("stockRealtimesByGroup").toList();

        if (response.size() > 0) {
            return new RestResponseDto<>().success(response);
        }

        return new RestResponseDto<>().badRequest();
    }

//    @PostMapping("synch")
//    public RestResponseDto<Object> synchData() {
//
//    }
}
