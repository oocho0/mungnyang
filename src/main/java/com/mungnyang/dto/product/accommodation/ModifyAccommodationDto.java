package com.mungnyang.dto.product.accommodation;

import com.mungnyang.dto.product.ModifyImageDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ModifyAccommodationDto {
    private Long accommodationId;
    private String accommodationName;
    private Long cityStateStateId;
    private Long cityCityId;
    private String productAddressAddressZipcode;
    private String productAddressAddressMain;
    private String productAddressAddressDetail;
    private String productAddressAddressExtra;
    private Double productAddressLon;
    private Double productAddressLat;
    private String checkInTime;
    private String checkOutTime;
    private String accommodationDetail;
    private String accommodationStatus;
    private List<ModifyImageDto> imageList;
    private List<FacilityDto> facilityList;
}
