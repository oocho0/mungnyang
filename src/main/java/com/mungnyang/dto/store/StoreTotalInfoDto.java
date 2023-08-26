package com.mungnyang.dto.store;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class StoreTotalInfoDto {

    private Long storeId;

    @NotBlank(message = "편의 시설 이름을 입력하지 않았습니다.")
    private String storeName;
    private String smallCategorySmallCategoryId;
    private String cityCityId;
    private String productAddressDetail;
    private String productAddressLon;
    private String productAddressLat;
    private String storeDetail;

    private List<StoreImageDto> storeImageDtoList = new ArrayList<>();
    private List<Long> storeImageIdList = new ArrayList<>();
}
