package com.mungnyang.dto.product.accommodation.room;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListRoomDto {
    private Long roomId;
    private String roomName;
    private Integer roomPrice;
    private String roomStatus;

    @QueryProjection
    public ListRoomDto(Long roomId, String roomName, Integer roomPrice, String roomStatus) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.roomPrice = roomPrice;
        this.roomStatus = roomStatus;
    }
}
