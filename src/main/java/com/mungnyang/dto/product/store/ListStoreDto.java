package com.mungnyang.dto.product.store;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListStoreDto {
    private Long storeId;
    private String storeName;
    private String bigCategoryName;
    private String smallCategoryName;
    private String stateName;
    private String cityName;
    private String storeStatus;
    private Float rate;
    private Long commentCount;

    @QueryProjection
    public ListStoreDto(Long storeId, String storeName, String bigCategoryName, String smallCategoryName, String stateName, String cityName, String storeStatus, Float rate, Long commentCount) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.bigCategoryName = bigCategoryName;
        this.smallCategoryName = smallCategoryName;
        this.stateName = stateName;
        this.cityName = cityName;
        this.storeStatus = storeStatus;
        this.rate = rate;
        this.commentCount = commentCount;
    }
}
