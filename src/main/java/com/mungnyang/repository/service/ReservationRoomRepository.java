package com.mungnyang.repository.service;

import com.mungnyang.entity.service.ReservationRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRoomRepository extends JpaRepository<ReservationRoom, Long> {

    List<ReservationRoom> findByRoomRoomIdAndCheckOutDateAfterOrderByReservationRoomId(Long roomId, LocalDateTime today);

    List<ReservationRoom> findByRoomRoomIdAndReservationNull(Long roomId);

    List<ReservationRoom> findByRoomRoomIdAndReservationNotNull(Long roomId);
}
