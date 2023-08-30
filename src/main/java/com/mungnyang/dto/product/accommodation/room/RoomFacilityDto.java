package com.mungnyang.dto.product.accommodation.room;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomFacilityDto {
    private Long roomFacilityId;
    private String facilityName;
    private String facilityIsExist;
    private Long roomId;
}
