package com.mungnyang.dto.product.accommodation.room;

import com.mungnyang.dto.service.InfoReservationRoomDto;
import lombok.*;
import org.springframework.data.domain.Page;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InfoRoomDto {
    private Long accommodationId;
    private String accommodationName;
    private Long roomId;
    private String roomName;
    private String roomStatus;
    private Long totalReservationCount;
    private Long pastReservationCount;
    private Long currentReservationCount;
    private Page<InfoReservationRoomDto> currentReservationRoomList;
    private Page<InfoReservationRoomDto> pastReservationRoomList;
}
