package com.mungnyang.repository.service;

import com.mungnyang.constant.ReservationStatus;
import com.mungnyang.entity.service.ReservationRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRoomRepository extends JpaRepository<ReservationRoom, Long>, QuerydslPredicateExecutor<ReservationRoom>, ReservationRoomPageRepository {
    List<ReservationRoom> findByRoomRoomIdAndCheckOutDateAfterAndReservationStatusOrderByReservationRoomIdDesc(Long roomId, LocalDateTime today, ReservationStatus reservation);
    List<ReservationRoom> findByRoomRoomIdAndCheckOutDateAfterAndReservationNullAndReservationStatus(Long roomId, LocalDateTime today, ReservationStatus reservation);
    List<ReservationRoom> findByRoomRoomIdAndCheckOutDateAfterAndReservationNotNullAndReservationStatus(Long roomId, LocalDateTime today, ReservationStatus reservation);
    List<ReservationRoom> findByRoomRoomIdAndReservationStatusOrderByReservationRoomId(Long roomId, ReservationStatus reservation);

    List<ReservationRoom> findByReservationReservationId(Long reservationId);
    Long countByRoomRoomIdAndReservationStatus(Long roomId, ReservationStatus reservation);
    Long countByRoomRoomIdAndCheckOutDateAfterAndReservationStatus(Long roomId, LocalDateTime today, ReservationStatus reservation);
    Long countByRoomAccommodationAccommodationIdAndReservationStatus(Long accommodationId, ReservationStatus reservation);
    Long countByRoomAccommodationAccommodationIdAndCheckOutDateAfterAndReservationStatus(Long accommodationId, LocalDateTime today, ReservationStatus reservation);
}
