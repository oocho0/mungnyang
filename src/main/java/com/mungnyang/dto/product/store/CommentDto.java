package com.mungnyang.dto.product.store;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private Long commentId;
    private String content;
    private Float rate;
    private String email;
}
