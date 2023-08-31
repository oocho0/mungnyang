package com.mungnyang.entity.product.accommodation;

import com.mungnyang.constant.Status;
import com.mungnyang.entity.fixedEntity.City;
import com.mungnyang.entity.product.Product;
import com.mungnyang.entity.product.ProductAddress;
import com.mungnyang.entity.fixedEntity.SmallCategory;
import com.mungnyang.entity.WriterEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "accommodation")
public class Accommodation extends WriterEntity implements Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long accommodationId;

    private String accommodationName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "small_category_id")
    private SmallCategory smallCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id")
    private City city;

    @Embedded
    @Column
    private ProductAddress productAddress;

    @Lob
    private String accommodationDetail;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status accommodationStatus;
}
