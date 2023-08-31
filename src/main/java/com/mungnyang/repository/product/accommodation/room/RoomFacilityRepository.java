package com.mungnyang.repository.product.accommodation.room;

import com.mungnyang.entity.product.accommodation.room.RoomFacility;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomFacilityRepository extends JpaRepository<RoomFacility, Long> {
    List<RoomFacility> findByRoomRoomIdOrderByRoomFacilityId(Long roomId);
}
