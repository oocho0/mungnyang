package com.mungnyang.entity.product.accommodation.room;

import com.mungnyang.entity.WriterEntity;
import com.mungnyang.entity.product.Facility;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "room_facility")
public class RoomFacility extends WriterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long roomFacilityId;

    @Embedded
    @Column
    private Facility facilityInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;
}
