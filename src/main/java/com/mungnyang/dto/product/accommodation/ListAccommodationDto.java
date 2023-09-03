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
    private Float rate;
    private Long commentCount;
    private List<ListRoomDto> rooms;

    @QueryProjection
    public ListAccommodationDto(Long accommodationId, String accommodationName, String stateName, String cityName, String accommodationStatus, Float rate, Long commentCount) {
        this.accommodationId = accommodationId;
        this.accommodationName = accommodationName;
        this.stateName = stateName;
        this.cityName = cityName;
        this.accommodationStatus = accommodationStatus;
        this.rate = rate;
        this.commentCount = commentCount;
    }
}
