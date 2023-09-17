package com.mungnyang.dto.service;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
public class InfoReservationRoomDto {
    private Long reservationRoomId;
    private Integer headCount;
    private LocalDateTime checkInDate;
    private LocalDateTime checkOutDate;
    private String memberName;
    private Integer days;

    @QueryProjection
    public InfoReservationRoomDto(Long reservationRoomId, Integer headCount, LocalDateTime checkInDate, LocalDateTime checkOutDate) {
        this.reservationRoomId = reservationRoomId;
        this.headCount = headCount;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }
}
