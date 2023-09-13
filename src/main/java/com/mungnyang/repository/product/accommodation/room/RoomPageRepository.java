package com.mungnyang.repository.product.accommodation.room;

import com.mungnyang.entity.product.accommodation.room.Room;

import java.time.LocalDateTime;
import java.util.List;

public interface RoomPageRepository {
    List<Room> getRoomAvailability(Long roomId, LocalDateTime inputCheckInDate, LocalDateTime inputCheckOutDate);
}
