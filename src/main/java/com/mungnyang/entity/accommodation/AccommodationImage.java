package com.mungnyang.entity.accommodation;

import com.mungnyang.entity.abstractEntity.WriterEntity;
import com.mungnyang.entity.embeddableEntity.Image;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
@Table(name = "accommodation_image")
public class AccommodationImage extends WriterEntity {

    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.AUTO)
    private Long accommodationImageId;

    @Embedded
    @Column
    private Image imageInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accommodation_id")
    private Accommodation accommodation;
}
