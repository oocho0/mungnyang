package com.mungnyang.dto.fixedEntityDto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MainSmallCategoryDto {
    private Long smallCategoryId;
    private String name;
    private Long productCount;
}
