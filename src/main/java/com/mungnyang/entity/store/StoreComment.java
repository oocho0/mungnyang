package com.mungnyang.entity.store;

import com.mungnyang.entity.base.WriterEntity;
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

    @Lob
    private String storeContent;

    private Float storeRate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store")
    private Long storeId;
}
