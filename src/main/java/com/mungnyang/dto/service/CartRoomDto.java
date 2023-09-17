package com.mungnyang.dto.service;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartRoomDto {
    private Long cartRoomId;
    private Long accommodationId;
    private String accommodationName;
    private String accommodationStatus;
    private Long roomId;
    private String roomName;
    private Integer roomPrice;
    private Integer headCount;
    private Integer days;
    private LocalDateTime checkInDate;
    private LocalDateTime checkOutDate;
    private String alreadyBooked;
    private String accommodationImage;
}
