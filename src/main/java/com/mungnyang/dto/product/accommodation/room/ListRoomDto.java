package com.mungnyang.dto.product.accommodation.room;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListRoomDto {
    private Long roomId;
    private String roomName;
    private Integer roomPrice;
    private Integer headCount;
    private String roomStatus;
}
