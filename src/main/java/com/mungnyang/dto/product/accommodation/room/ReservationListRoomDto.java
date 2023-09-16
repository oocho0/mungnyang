package com.mungnyang.dto.product.accommodation.room;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationListRoomDto {
    private Long roomId;
    private String roomName;
    private String roomStatus;
    private Long totalReservationCount;
    private Long pastReservationCount;
    private Long currentReservationCount;
}
