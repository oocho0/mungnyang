package com.mungnyang.dto.product;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

@Getter
@Setter
public class ResultDto {
    private Long id;
    private String name;
    private String category;
    private Float rate;
    private Long commentCount;
    private String status;
    private String address;
    private Double lon;
    private Double lat;
    private String repImageUrl;

    @QueryProjection
    public ResultDto(Long id, String name, String category, Float rate, Long commentCount, String status, String address, Double lon, Double lat, String repImageUrl) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.rate = rate;
        this.commentCount = commentCount;
        this.status = status;
        this.address = address;
        this.lon = lon;
        this.lat = lat;
        this.repImageUrl = repImageUrl;
    }
}
