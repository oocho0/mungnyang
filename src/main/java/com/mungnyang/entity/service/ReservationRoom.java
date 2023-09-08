package com.mungnyang.entity.service;

import com.mungnyang.entity.WriterEntity;
import com.mungnyang.entity.product.accommodation.room.Room;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name="reservation_room")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationRoom extends WriterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long reservationRoomId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    private Integer reservationPrice;
    private LocalDateTime checkInDate;
    private LocalDateTime checkOutDate;
}
