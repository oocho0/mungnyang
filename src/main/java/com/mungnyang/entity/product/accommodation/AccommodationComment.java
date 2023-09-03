package com.mungnyang.entity.product.accommodation;

import com.mungnyang.entity.WriterEntity;
import com.mungnyang.entity.product.Comment;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "accommodation_comment")
public class AccommodationComment extends WriterEntity {

    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private Long accommodationCommentId;

    @Embedded
    @Column
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accommodation_id")
    private Accommodation accommodation;
}
