package com.mungnyang.dto.product.accommodation;

import com.mungnyang.dto.product.accommodation.room.ReservationListRoomDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationListAccommodationDto {
    private Long accommodationId;
    private String accommodationName;
    private String accommodationStatus;
    private Long totalReservationCount;
    private Long pastReservationCount;
    private Long currentReservationCount;
    private List<ReservationListRoomDto> roomList;
}
