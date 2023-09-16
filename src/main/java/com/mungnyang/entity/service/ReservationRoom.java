package com.mungnyang.entity.service;

import com.mungnyang.constant.ReservationStatus;
import com.mungnyang.entity.WriterEntity;
import com.mungnyang.entity.product.accommodation.room.Room;
import lombok.*;
import org.springframework.stereotype.Controller;

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

    private Integer headCount;
    private LocalDateTime checkInDate;
    private LocalDateTime checkOutDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus reservationStatus;
}
