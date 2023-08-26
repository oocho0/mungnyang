package com.mungnyang.entity.store;

import com.mungnyang.entity.abstractEntity.WriterEntity;
import com.mungnyang.entity.embeddableEntity.Comment;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "store_comment")
public class StoreComment extends WriterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long storeCommentId;

    @Embedded
    @Column
    private Comment commentInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;
}
