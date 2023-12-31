package com.mungnyang.entity.product.store;

import com.mungnyang.entity.WriterEntity;
import com.mungnyang.entity.member.Member;
import com.mungnyang.entity.product.Comment;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "store_comment")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StoreComment extends WriterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long storeCommentId;

    @Embedded
    @Column
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;
}
