package com.mungnyang.repository.service;

import com.mungnyang.entity.service.ReservationRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRoomRepository extends JpaRepository<ReservationRoom, Long> {

    List<ReservationRoom> findByRoomRoomIdOrderByReservationRoomId(Long roomId);
}
