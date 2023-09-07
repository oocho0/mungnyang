package com.mungnyang.dto.product.accommodation;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ModifyFacilityDto {
    private Long facilityId;
    private String facilityName;
    private String isDelete;
}
