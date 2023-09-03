package com.mungnyang.dto.product.accommodation;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccommodationImageDto {
    private Long accommodationImageId;
    private String imageName;
    private String imageFileName;
    private String imageUrl;
    private String imageIsRepresentative;
}
