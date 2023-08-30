package com.mungnyang.dto.product.accommodation;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CreateAccommodationDto {

    private String accommodationName;

    private Long smallCategoryId;

    private String productAddressAddressZipcode;
    private String productAddressAddressMain;
    private String productAddressAddressDetail;
    private String productAddressAddressExtra;
    private String productAddressLon;
    private String productAddressLat;

    private String accommodationDetail;
}
