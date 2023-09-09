package com.mungnyang.dto.fixedEntityDto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MainBigCategoryDto {
    private Long bigCategoryId;
    private String name;
    private Long productCount;
}
