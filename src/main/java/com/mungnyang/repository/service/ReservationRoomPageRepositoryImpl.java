package com.mungnyang.repository.service;

import com.mungnyang.constant.ReservationStatus;
import com.mungnyang.dto.service.InfoReservationRoomDto;
import com.mungnyang.dto.service.QInfoReservationRoomDto;
import com.mungnyang.entity.service.QReservationRoom;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.List;

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

    @Override
    public Page<InfoReservationRoomDto> getCurrentInfoReservationRoomDto(Long roomId, Pageable pageable) {
        List<InfoReservationRoomDto> results = jpaQueryFactory.select(
                    new QInfoReservationRoomDto(
                            reservationRoom.reservationRoomId,
                            reservationRoom.headCount,
                            reservationRoom.checkInDate,
                            reservationRoom.checkOutDate
                    )
                ).from(reservationRoom)
                .where(reservationRoom.room.roomId.eq(roomId))
                .where(reservationRoom.checkOutDate.after(LocalDateTime.now()))
                .where(reservationRoom.reservationStatus.eq(ReservationStatus.RESERVATION))
                .orderBy(reservationRoom.checkOutDate.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        JPAQuery<Long> counts = jpaQueryFactory.select(reservationRoom.count())
                .from(reservationRoom)
                .where(reservationRoom.room.roomId.eq(roomId))
                .where(reservationRoom.checkOutDate.after(LocalDateTime.now()))
                .where(reservationRoom.reservationStatus.eq(ReservationStatus.RESERVATION));
        return PageableExecutionUtils.getPage(results, pageable, counts::fetchOne);
    }

    @Override
    public Page<InfoReservationRoomDto> getPastInfoReservationRoomDto(Long roomId, Pageable pageable) {
        List<InfoReservationRoomDto> results = jpaQueryFactory.select(
                        new QInfoReservationRoomDto(
                                reservationRoom.reservationRoomId,
                                reservationRoom.headCount,
                                reservationRoom.checkInDate,
                                reservationRoom.checkOutDate
                        )
                ).from(reservationRoom)
                .where(reservationRoom.room.roomId.eq(roomId))
                .where(reservationRoom.checkOutDate.before(LocalDateTime.now()))
                .where(reservationRoom.reservationStatus.eq(ReservationStatus.RESERVATION))
                .orderBy(reservationRoom.checkOutDate.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        JPAQuery<Long> counts = jpaQueryFactory.select(reservationRoom.count())
                .from(reservationRoom)
                .where(reservationRoom.room.roomId.eq(roomId))
                .where(reservationRoom.checkOutDate.before(LocalDateTime.now()))
                .where(reservationRoom.reservationStatus.eq(ReservationStatus.RESERVATION));
        return PageableExecutionUtils.getPage(results, pageable, counts::fetchOne);
    }
}
