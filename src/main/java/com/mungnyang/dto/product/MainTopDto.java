package com.mungnyang.dto.product;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MainTopDto {
    private String name;
    private List<TopInfoDto> info;
}
