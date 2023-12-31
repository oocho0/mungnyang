package com.mungnyang.dto.product.store;

import com.mungnyang.dto.product.ModifyImageDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ModifyStoreDto {

    private Long storeId;
    private String storeName;
    private Long smallCategoryBigCategoryBigCategoryId;
    private Long smallCategorySmallCategoryId;
    private Long cityStateStateId;
    private Long cityCityId;
    private String productAddressAddressZipcode;
    private String productAddressAddressMain;
    private String productAddressAddressDetail;
    private String productAddressAddressExtra;
    private Double productAddressLon;
    private Double productAddressLat;
    private String storeDetail;
    private String storeStatus;
    private List<ModifyImageDto> imageList;
}
