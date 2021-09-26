package com.hoang.webSocket.market;

import com.hoang.webSocket.entity.StockRealtimeEntity;
import com.hoang.webSocket.repository.StockRealtimeRepository;
import com.hoang.webSocket.wsConfig.IMarketWebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.security.SecureRandom;

@Service
public class FakeSocketDataMessage {
    @Value("#{'${market.fake-data.enable}'.trim()}")
    private boolean fakeEnable;

    private final SecureRandom secureRandom = new SecureRandom();

    @Autowired
    StockRealtimeRepository stockRealtimeRepository;

    @Autowired
    IMarketWebSocketServer marketWebSocketServer;

    @Scheduled(fixedRate = 30, initialDelayString = "#{ T(java.util.concurrent.ThreadLocalRandom).current().nextInt(60) }")
    private void getData() {
        if (fakeEnable) {
            long count = stockRealtimeRepository.count();
            StockRealtimeEntity data = stockRealtimeRepository.findRandom(PageRequest.of(secureRandom.nextInt((int) count), 1)).get(0);

            boolean hasData = false;

            for (Field field : data.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                int random = secureRandom.nextInt(30);

                if (field.getType().equals(String.class) && !field.getName().equals("s")) {
                    try {
                        field.set(data, (Object) null);
                    } catch (IllegalAccessException ignored){

                    }
                }

                if (random != 5) {
                    if (!field.getType().equals(String.class)) {
                        try {
                            field.set(data, (Object) null);
                        } catch (IllegalAccessException ignored) {
                        }
                    }
                } else {
                    if (field.getType().equals(Float.class)) {
                        try {
                            field.set(data, getRandomFloat(100F));
                            hasData = true;
                        } catch (IllegalAccessException ignored) {
                        }
                    } else if (field.getType().equals(Long.class)) {
                        try {
                            field.set(data, getRandomLong(1000L));
                            hasData = true;
                        } catch (IllegalAccessException ignored) {
                        }
                    }
                }
            }
            if (hasData) {
                marketWebSocketServer.stackData(data);
            }
        }
    }

    private Float getRandomFloat(Float avg) {
        return avg + secureRandom.nextFloat() * avg / 20;
    }

    private Long getRandomLong(Long avg) {
        return avg + secureRandom.nextInt((int) (avg / 20));
    }


}
