package com.mungnyang.dto.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class CreateReservationDto {
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime reservationDate;
    private Integer reservationTotalPrice;
    private List<CreateReservationRoomDto> reservationRoomList;
}
