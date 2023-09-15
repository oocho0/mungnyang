package com.mungnyang.dto.product.accommodation.room;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AvailableRoomDto {
    private Long roomId;
    private String isHeadCountCapable;
    private String isAvailable;
}
