package com.mungnyang.dto.product.store;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CreateStoreDto {

    private Long storeId;

    private String storeName;

    private Long smallCategoryId;

    private String productAddressAddressZipcode;
    private String productAddressAddressMain;
    private String productAddressAddressDetail;
    private String productAddressAddressExtra;
    private String productAddressLon;
    private String productAddressLat;

    private String storeDetail;
}
