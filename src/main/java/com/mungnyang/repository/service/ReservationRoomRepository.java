package com.mungnyang.repository.service;

import com.mungnyang.constant.ReservationStatus;
import com.mungnyang.entity.service.ReservationRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRoomRepository extends JpaRepository<ReservationRoom, Long>, QuerydslPredicateExecutor<ReservationRoom>, ReservationRoomPageRepository {
    List<ReservationRoom> findByRoomRoomIdAndCheckOutDateAfterAndReservationStatusOrderByReservationRoomId(Long roomId, LocalDateTime today, ReservationStatus reservationStatus);
    List<ReservationRoom> findByRoomRoomIdAndReservationNullAndReservationStatus(Long roomId, ReservationStatus reservationStatus);
    List<ReservationRoom> findByRoomRoomIdAndReservationNotNullAndReservationStatus(Long roomId, ReservationStatus reservationStatus);
    List<ReservationRoom> findByReservationReservationId(Long reservationId);
}
