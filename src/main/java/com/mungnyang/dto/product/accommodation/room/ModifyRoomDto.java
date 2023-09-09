package com.mungnyang.dto.product.accommodation.room;

import com.mungnyang.dto.product.ModifyImageDto;
import com.mungnyang.dto.product.accommodation.ModifyFacilityDto;
import com.mungnyang.dto.service.ModifyReservationRoomDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ModifyRoomDto {
    private Long accommodationId;
    private Long roomId;
    private String roomName;
    private Integer roomPrice;
    private Integer roomPeople;
    private String roomDetail;
    private String roomStatus;
    private List<ModifyImageDto> imageList;
    private List<ModifyFacilityDto> facilityList;
    private List<ModifyReservationRoomDto> reservationList;
}
