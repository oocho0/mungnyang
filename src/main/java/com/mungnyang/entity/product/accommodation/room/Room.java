package com.mungnyang.entity.product.accommodation.room;

import com.mungnyang.constant.Status;
import com.mungnyang.entity.WriterEntity;
import com.mungnyang.entity.product.Product;
import com.mungnyang.entity.product.accommodation.Accommodation;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "room")
public class Room extends WriterEntity implements Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long roomId;

    private String roomName;
    private Integer roomPrice;
    private Integer roomPeople;
    @Lob
    private String roomDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accommodation_id")
    private Accommodation accommodation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status roomStatus;
}
