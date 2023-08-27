package com.mungnyang.dto.product.store;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class StoreTotalInfoDto {

    private Long storeId;

    @NotBlank(message = "편의 시설 이름을 입력하지 않았습니다.")
    @Length(max = 50, message = "허용된 글자수를 넘었습니다.")
    private String storeName;

    @NotEmpty(message = "편의 시설 분류를 입력하지 않았습니다.")
    private String smallCategorySmallCategoryId;

    @NotEmpty(message = "주소 분류를 입력하지 않았습니다.")
    private String cityCityId;

    @NotBlank(message = "상세 주소를 입력하지 않았습니다.")
    private String productAddressDetail;
    @NotBlank(message = "경도를 입력하지 않았습니다.")
    private String productAddressLon;
    @NotBlank(message = "위도를 입력하지 않았습니다.")
    private String productAddressLat;

    private String storeDetail;

    private List<StoreImageDto> storeImageDtoList = new ArrayList<>();
    private List<Long> storeImageIdList = new ArrayList<>();
}
