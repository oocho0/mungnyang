package com.mungnyang.entity.product.accommodation.room;

import com.mungnyang.entity.WriterEntity;
import com.mungnyang.entity.product.Image;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "room_image")
public class RoomImage extends WriterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long roomImageId;

    @Embedded
    @Column
    private Image image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;
}
