package com.mungnyang.dto.product;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ModifyImageDto {
    private Long imageId;
    private String isDelete;
    private MultipartFile imageFile;
}
