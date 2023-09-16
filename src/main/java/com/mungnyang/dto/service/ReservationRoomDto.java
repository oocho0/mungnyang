package com.mungnyang.dto.service;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationRoomDto {
    private Long reservationRoomId;
    private Long accommodationId;
    private String accommodationName;
    private String accommodationImage;
    private Long roomId;
    private String roomName;
    private Integer roomPrice;
    private Integer days;
    private Integer headCount;
    private LocalDateTime checkInDate;
    private LocalDateTime checkOutDate;
}
