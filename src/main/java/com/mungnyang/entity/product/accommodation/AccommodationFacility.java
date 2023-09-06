package com.mungnyang.entity.product.accommodation;

import com.mungnyang.entity.WriterEntity;
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

    private String facilityName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accommodation_id")
    private Accommodation accommodation;
}
