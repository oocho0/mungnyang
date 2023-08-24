package com.mungnyang.entity.store;

import com.mungnyang.entity.base.WriterEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "store_image")
public class StoreImage extends WriterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long storeImgId;

    private String imgName;
    private String originalImgName;
    private String imgUrl;
    private String isRepresentativeImg;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store")
    private Long storeId;
}
