package com.mungnyang.entity.product;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.persistence.Lob;
import java.util.Objects;

@Embeddable
@Getter
@Setter
public class Comment {

    @Lob
    private String commentContent;
    private Float rate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(commentContent, comment.commentContent) && Objects.equals(rate, comment.rate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commentContent, rate);
    }
}
