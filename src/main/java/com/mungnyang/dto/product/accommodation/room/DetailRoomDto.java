package com.mungnyang.dto.product.accommodation.room;

import lombok.*;

import java.time.LocalDateTime;
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
    private Integer people;
    private String isPeopleCapable;
    private String isAvailable;
    private String detail;
    private String status;
    private List<String> images;
    private List<String> facilities;
}
