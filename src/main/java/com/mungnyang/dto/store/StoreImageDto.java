package com.mungnyang.dto.store;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreImageDto {
    private Long storeImageId;
    private String imageName;
    private String imageFileName;
    private String imageUrl;
    private String imageIsRepresentative;
}
