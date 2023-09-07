package com.mungnyang.repository.product.accommodation.room;

import com.mungnyang.constant.Status;
import com.mungnyang.entity.product.accommodation.room.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {

    Room findByRoomIdAndRoomStatusNot(Long roomId, Status closed);
    List<Room> findByAccommodationAccommodationIdAndRoomStatusNotOrderByRoomId(Long accommodationId, Status closed);
}
