package com.mungnyang.entity.service;

import com.mungnyang.entity.WriterEntity;
import com.mungnyang.entity.product.accommodation.room.Room;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cart_room")
public class CartRoom extends WriterEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long cartRoomId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    private Integer headCount;
    private LocalDateTime checkInDate;
    private LocalDateTime checkOutDate;
}
