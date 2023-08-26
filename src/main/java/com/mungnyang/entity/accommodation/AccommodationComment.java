package com.mungnyang.entity.accommodation;

import com.mungnyang.entity.abstractEntity.WriterEntity;
import com.mungnyang.entity.embeddableEntity.Comment;
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
    private Comment commentInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accommodation_id")
    private Accommodation accommodation;
}
