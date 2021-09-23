package com.hoang.webSocket.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity(name = "StockRealtime")
@Table(name = "StockRealtime")
public class StockRealtimeEntity {

    @Id
    @Column(name = "symbol")

    private String s; // symbol

    @Column(name = "stockNo")

    private String no; // stockNo

    @Column(name = "exchange")

    private String e; // type

    @Column(name = "type")

    private Integer t; // type

    @Column(name = "ceiling_price")

    private Float c; // ceil

    @Column(name = "floor_price")
    private Float f; // floor

    @Column(name = "ref_price")
    private Float r; // reference

    @Column(name = "high_price")
    private Float h; // high

    @Column(name = "avg_price")
    private Float a; // average

    @Column(name = "low_price")

    private Float l; // low
    @Column(name = "match_price")

    private Float mp; // match price
    @Column(name = "match_vol")

    private Long mv; // match vol

    @Column(name = "total_qty")
    private Long tq; // total qty

    @Column(name = "sell_price_1")
    private Float sp1; // sell price 1

    @Column(name = "sell_vol_1")
    private Long sv1; // sell vol 1

    @Column(name = "sell_price_2")
    private Float sp2;
    @Column(name = "sell_vol_2")

    private Long sv2;

    @Column(name = "sell_price_3")

    private Float sp3;
    @Column(name = "sell_vol_3")

    private Long sv3;

    @Column(name = "sell_price_4")

    private Float sp4;
    @Column(name = "sell_vol_4")

    private Long sv4;

    @Column(name = "sell_price_5")

    private Float sp5;
    @Column(name = "sell_vol_5")

    private Long sv5;

    @Column(name = "sell_price_6")

    private Float sp6;

    @Column(name = "sell_vol_6")
    private Long sv6;

    @Column(name = "sell_price_7")

    private Float sp7;
    @Column(name = "sell_vol_7")

    private Long sv7;

    @Column(name = "sell_price_8")

    private Float sp8;
    @Column(name = "sell_vol_8")

    private Long sv8;

    @Column(name = "sell_price_9")

    private Float sp9;
    @Column(name = "sell_vol_9")

    private Long sv9;

    @Column(name = "sell_price_10")

    private Float sp10;
    @Column(name = "sell_vol_10")

    private Long sv10;

    @Column(name = "buy_price_1")

    private Float bp1; // buy price 1
    @Column(name = "buy_vol_1")

    private Long bv1; // buy vol 1

    @Column(name = "buy_price_2")

    private Float bp2;
    @Column(name = "buy_vol_2")

    private Long bv2;

    @Column(name = "buy_price_3")

    private Float bp3;
    @Column(name = "buy_vol_3")

    private Long bv3;

    @Column(name = "buy_price_4")

    private Float bp4;
    @Column(name = "buy_vol_4")

    private Long bv4;

    @Column(name = "buy_price_5")

    private Float bp5;
    @Column(name = "buy_vol_5")

    private Long bv5;

    @Column(name = "buy_price_6")

    private Float bp6;
    @Column(name = "buy_vol_6")

    private Long bv6;

    @Column(name = "buy_price_7")

    private Float bp7;
    @Column(name = "buy_vol_7")

    private Long bv7;

    @Column(name = "buy_price_8")

    private Float bp8;
    @Column(name = "buy_vol_8")

    private Long bv8;

    @Column(name = "buy_price_9")

    private Float bp9;
    @Column(name = "buy_vol_9")

    private Long bv9;

    @Column(name = "buy_price_10")

    private Float bp10;
    @Column(name = "buy_vol_10")

    private Long bv10;


    private Long sq;

    private Long bq;


    private Long fsq;

    private Long fbq;


    private Long rfq;

}
