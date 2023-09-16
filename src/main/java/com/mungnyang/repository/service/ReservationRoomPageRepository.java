package com.mungnyang.repository.service;

import com.mungnyang.dto.service.InfoReservationRoomDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface ReservationRoomPageRepository {
    Long findReservationRoomForCartRoom(Long roomId, LocalDateTime checkInDate, LocalDateTime checkOutDate);

    Page<InfoReservationRoomDto> getCurrentInfoReservationRoomDto(Long roomId, Pageable pageable);

    Page<InfoReservationRoomDto> getPastInfoReservationRoomDto(Long roomId, Pageable pageable);
}
