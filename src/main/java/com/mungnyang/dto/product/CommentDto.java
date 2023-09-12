package com.mungnyang.dto.product;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentDto {
    private Long commentId;
    private String content;
    private Float rate;
    private String email;
    private LocalDateTime createDate;
    @QueryProjection
    public CommentDto(Long commentId, String content, Float rate, String email, LocalDateTime createDate) {
        this.commentId = commentId;
        this.content = content;
        this.rate = rate;
        this.email = email;
        this.createDate = createDate;
    }
}
