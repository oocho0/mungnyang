package com.mungnyang.repository.service;

import com.mungnyang.constant.ReservationStatus;
import com.mungnyang.entity.service.QReservationRoom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
public class ReservationRoomPageRepositoryImpl implements ReservationRoomPageRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private final QReservationRoom reservationRoom = QReservationRoom.reservationRoom;
    @Override
    public Long findReservationRoomForCartRoom(Long roomId, LocalDateTime checkInDate, LocalDateTime checkOutDate) {
        return jpaQueryFactory.select(reservationRoom.count()).from(reservationRoom)
                .where(reservationRoom.room.roomId.eq(roomId))
                .where(reservationRoom.reservationStatus.eq(ReservationStatus.RESERVATION))
                .where(reservationRoom.checkInDate.eq(checkInDate)
                        .or(reservationRoom.checkOutDate.eq(checkOutDate))
                        .or(reservationRoom.checkInDate.between(checkInDate, checkOutDate))
                        .or(reservationRoom.checkOutDate.between(checkInDate, checkOutDate))
                        .or(reservationRoom.checkInDate.before(checkInDate).and(reservationRoom.checkOutDate.after(checkInDate)))
                        .or(reservationRoom.checkInDate.before(checkOutDate).and(reservationRoom.checkOutDate.after(checkOutDate))))
                .fetchOne();
    }
}
