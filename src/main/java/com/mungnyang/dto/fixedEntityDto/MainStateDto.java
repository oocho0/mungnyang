package com.mungnyang.dto.fixedEntityDto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MainStateDto {
    private Long stateId;
    private String name;
    private Long productCount;
}
