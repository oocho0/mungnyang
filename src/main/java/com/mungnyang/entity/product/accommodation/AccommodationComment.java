package com.mungnyang.entity.product.accommodation;

import com.mungnyang.entity.WriterEntity;
import com.mungnyang.entity.member.Member;
import com.mungnyang.entity.product.Comment;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "accommodation_comment")
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
