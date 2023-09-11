package com.mungnyang.dto.product.store;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DetailStoreDto {
    private Long id;
    private String name;
    private String category;
    private String address;
    private String detail;
    private String status;
    private List<String> images;
    private List<CommentDto> comments;
}
