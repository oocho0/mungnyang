package com.mungnyang.entity.room;

import com.mungnyang.entity.abstractEntity.WriterEntity;
import com.mungnyang.entity.accommodation.Accommodation;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "room")
public class Room extends WriterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long roomId;

    private String roomName;
    private Integer roomPrice;
    private String roomDetail;
    private String isAvailable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accommodation_id")
    private Accommodation accommodation;
}
