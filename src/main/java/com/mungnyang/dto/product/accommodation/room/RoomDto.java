package com.mungnyang.dto.product.accommodation.room;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomDto {

    private Long roomId;
    private String roomName;
    private Integer roomPrice;
    private String roomDetail;
    private String isAvailable;
}
