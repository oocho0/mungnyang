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
    private String userName;
    private LocalDateTime createDate;
    @QueryProjection
    public CommentDto(Long commentId, String content, Float rate, String email, String userName, LocalDateTime createDate) {
        this.commentId = commentId;
        this.content = content;
        this.rate = rate;
        this.email = email;
        this.userName = userName;
        this.createDate = createDate;
    }
}
