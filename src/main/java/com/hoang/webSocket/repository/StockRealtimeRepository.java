package com.hoang.webSocket.repository;

import com.hoang.webSocket.entity.StockRealtimeEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StockRealtimeRepository extends CrudRepository<StockRealtimeEntity, String> {
    @Query("SELECT item FROM StockRealtime item WHERE item.s IN :stockCodes")
    List<StockRealtimeEntity> findByStockCodes(@Param("stockCodes") List<String> stockCodes);

    @Query("SELECT item  FROM StockRealtime item")
    List<StockRealtimeEntity> findRandom(Pageable pageable);

}
