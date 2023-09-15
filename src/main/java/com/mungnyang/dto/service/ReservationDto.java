package com.mungnyang.dto.service;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDto {
    private Long reservationId;
    private LocalDateTime reservationDate;
    private String reservationStatus;
    private Integer reservationTotalPrice;
    private List<ReservationRoomDto> reservationRoomList;
}
