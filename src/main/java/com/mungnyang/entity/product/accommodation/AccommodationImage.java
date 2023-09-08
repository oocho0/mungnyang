package com.mungnyang.entity.product.accommodation;

import com.mungnyang.entity.WriterEntity;
import com.mungnyang.entity.product.Image;
import com.mungnyang.entity.product.ProductImage;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
@Table(name = "accommodation_image")
public class AccommodationImage extends WriterEntity implements ProductImage {

    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.AUTO)
    private Long accommodationImageId;

    @Embedded
    @Column
    private Image image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accommodation_id")
    private Accommodation accommodation;
}
