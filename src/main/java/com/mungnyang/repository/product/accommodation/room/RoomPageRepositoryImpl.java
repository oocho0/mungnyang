package com.mungnyang.repository.product.accommodation.room;

import com.mungnyang.constant.ReservationStatus;
import com.mungnyang.entity.product.accommodation.room.QRoom;
import com.mungnyang.entity.product.accommodation.room.Room;
import com.mungnyang.entity.service.QReservationRoom;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class RoomPageRepositoryImpl implements RoomPageRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private final QRoom room = QRoom.room;
    private final QReservationRoom reservationRoom = QReservationRoom.reservationRoom;

    private BooleanExpression searchByCheckInTime(LocalDateTime checkInTime, LocalDateTime checkOutDate) {
        if (checkInTime == null || checkOutDate == null) {
            return null;
        }
        return room.roomId.notIn(
                jpaQueryFactory.select(room.roomId)
                        .from(room)
                        .leftJoin(reservationRoom).on(room.roomId.eq(reservationRoom.room.roomId))
                        .where(reservationRoom.reservationStatus.eq(ReservationStatus.RESERVATION))
                        .where(reservationRoom.checkInDate.eq(checkInTime)
                                .or(reservationRoom.checkOutDate.eq(checkOutDate))
                                .or(reservationRoom.checkInDate.between(checkInTime, checkOutDate))
                                .or(reservationRoom.checkOutDate.between(checkInTime, checkOutDate))
                                .or(reservationRoom.checkInDate.before(checkInTime).and(reservationRoom.checkOutDate.after(checkInTime)))
                                .or(reservationRoom.checkInDate.before(checkOutDate).and(reservationRoom.checkOutDate.after(checkOutDate)))));
    }

    @Override
    public List<Room> getRoomAvailability(Long roomId, LocalDateTime inputCheckInDate, LocalDateTime inputCheckOutDate) {
        return jpaQueryFactory.selectFrom(room)
                .where(room.roomId.eq(roomId),
                        searchByCheckInTime(inputCheckInDate, inputCheckOutDate))
                .fetch();
    }
}
