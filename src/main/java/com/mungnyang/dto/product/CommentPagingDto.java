package com.mungnyang.dto.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
@AllArgsConstructor
public class CommentPagingDto {
    private Page<CommentDto> comments;
    private String email;
}
