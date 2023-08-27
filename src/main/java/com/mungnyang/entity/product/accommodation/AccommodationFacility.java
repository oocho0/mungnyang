package com.mungnyang.entity.product.accommodation;

import com.mungnyang.entity.WriterEntity;
import com.mungnyang.entity.product.Facility;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "accommodation_facility")
public class AccommodationFacility extends WriterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long accommodationFacilityId;

    @Embedded
    @Column
    private Facility facilityInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accommodation_id")
    private Accommodation accommodation;
}
