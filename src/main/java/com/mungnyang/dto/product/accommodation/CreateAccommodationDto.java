package com.mungnyang.dto.product.accommodation;

import com.mungnyang.dto.product.accommodation.room.RoomDto;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CreateAccommodationDto {

    private Long accommodationId;

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
