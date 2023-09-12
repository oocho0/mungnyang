package com.mungnyang.dto.product.store;

import com.mungnyang.dto.product.CommentDto;
import lombok.*;
import org.springframework.data.domain.Page;

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
    private Double lat;
    private Double lon;
    private String detail;
    private String status;
    private Float rateAvg;
    private Long commentCount;
    private List<String> images;
    private Page<CommentDto> comments;
}
