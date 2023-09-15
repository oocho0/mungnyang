package com.mungnyang.dto.product.accommodation.room;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DetailRoomDto {
    private Long id;
    private String name;
    private Integer price;
    private Integer headCount;
    private String isHeadCountCapable;
    private String isAvailable;
    private String detail;
    private String status;
    private List<String> images;
    private List<String> facilities;
}
