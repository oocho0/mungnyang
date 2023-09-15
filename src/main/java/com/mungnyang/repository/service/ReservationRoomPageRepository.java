package com.mungnyang.repository.service;

import java.time.LocalDateTime;

public interface ReservationRoomPageRepository {
    Long findReservationRoomForCartRoom(Long roomId, LocalDateTime checkInDate, LocalDateTime checkOutDate);
}
