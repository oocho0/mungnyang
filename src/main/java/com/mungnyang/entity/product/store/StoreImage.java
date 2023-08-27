package com.mungnyang.entity.product.store;

import com.mungnyang.entity.WriterEntity;
import com.mungnyang.entity.product.Image;
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
    private Long storeImageId;

    @Embedded
    @Column
    private Image image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;
}
