package com.mungnyang.dto.product.accommodation.room;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RoomImageDto {
    private Long roomImageId;
    private String imageName;
    private String imageFileName;
    private String imageUrl;
    private String imageIsRepresentative;
}
