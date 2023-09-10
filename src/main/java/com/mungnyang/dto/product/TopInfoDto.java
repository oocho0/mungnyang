package com.mungnyang.dto.product;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

@Setter
@Getter
public class TopInfoDto {
    private String name;
    private String address;
    private Float rate;
    private Long commentCount;
    private String repImageUrl;

    @QueryProjection
    public TopInfoDto(String name, String address, Float rate, Long commentCount, String repImageUrl) {
        this.name = name;
        this.address = address;
        this.rate = rate;
        this.commentCount = commentCount;
        this.repImageUrl = repImageUrl;
    }
}
