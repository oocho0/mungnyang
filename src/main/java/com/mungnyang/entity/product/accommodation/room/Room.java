package com.mungnyang.entity.product.accommodation.room;

import com.mungnyang.entity.WriterEntity;
import com.mungnyang.entity.product.accommodation.Accommodation;
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
    @Lob
    private String roomDetail;
    private String isAvailable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accommodation_id")
    private Accommodation accommodation;
}
