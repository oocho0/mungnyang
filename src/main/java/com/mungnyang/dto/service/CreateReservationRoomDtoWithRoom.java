package com.mungnyang.dto.service;

import com.mungnyang.entity.product.accommodation.room.Room;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
public class CreateReservationRoomDtoWithRoom {
    private Long accommodationId;
    private Room room;
    private Long cartRoomId;
    private Integer days;
    private Integer headCount;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime checkInDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime checkOutDate;
}
