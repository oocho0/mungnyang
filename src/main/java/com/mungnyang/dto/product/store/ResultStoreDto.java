package com.mungnyang.dto.product.store;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

@Getter
@Setter
public class ResultStoreDto {
    private Long storeId;
    private String storeName;
    private String category;
    private Float rate;
    private Long commentCount;
    private String address;
    private Double lon;
    private Double lat;

    @QueryProjection
    public ResultStoreDto(Long storeId, String storeName, String category, Float rate, Long commentCount, String address, Double lon, Double lat) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.category = category;
        this.rate = rate;
        this.commentCount = commentCount;
        this.address = address;
        this.lon = lon;
        this.lat = lat;
    }
}
