package com.hoang.webSocket.controller;

import com.hoang.webSocket.dto.RestResponseDto;

import org.json.JSONObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@Validated
@RequestMapping("api/market")
public class DataController {

    RestTemplate restTemplate = new RestTemplate();

    @GetMapping("info")
    public RestResponseDto<Object> getCodeInfo() {

        String data = restTemplate.getForObject("https://api-finfo.vndirect.com.vn/v4/stocks?q=type:IFC,ETF,STOCK~status:LISTED&size=10000",
                String.class);
        JSONObject response = new JSONObject(data);

        return new RestResponseDto<>().success(response.getJSONArray("data").toList());
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

}
