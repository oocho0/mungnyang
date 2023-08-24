package com.mungnyang.entity.store;

import com.mungnyang.entity.base.WriterEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "store")
public class Store extends WriterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long storeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city")
    private String storeName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "small_category")
    private Long smallCategoryId;


    private Long cityId;
    @Lob
    private String storeRestAddress;

    private Double storeIon;
    private Double storeLat;

    private String storeDetail;
}
