package com.mungnyang.entity.store;

import com.mungnyang.entity.abstractEntity.WriterEntity;
import com.mungnyang.entity.embeddableEntity.Image;
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
