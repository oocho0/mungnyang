package com.mungnyang.dto.product;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ModifyImageDto {
    private Long imageId;
    private String imageFileName;
    private String isDelete;
    private MultipartFile imageFile;
}
