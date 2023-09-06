package com.mungnyang.dto.product.accommodation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class FacilityDto {
    private Long facilityId;
    private String facilityName;
    private Long includedId;
}
