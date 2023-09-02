package com.mungnyang.dto.product.accommodation;

import com.mungnyang.dto.product.accommodation.room.ListRoomDto;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListAccommodationDto {
    private Long accommodationId;
    private String accommodationName;
    private String stateName;
    private String cityName;
    private String accommodationStatus;
    private List<ListRoomDto> rooms;
    private Float rate;
    private Long commentCount;

    @QueryProjection
    public ListAccommodationDto(Long accommodationId, String accommodationName, String stateName, String cityName, String accommodationStatus) {
        this.accommodationId = accommodationId;
        this.accommodationName = accommodationName;
        this.stateName = stateName;
        this.cityName = cityName;
        this.accommodationStatus = accommodationStatus;
    }
}
