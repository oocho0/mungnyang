package com.mungnyang.dto.fixedEntityDto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MainCityDto {
    private Long cityId;
    private String name;
    private Long productCount;
}
