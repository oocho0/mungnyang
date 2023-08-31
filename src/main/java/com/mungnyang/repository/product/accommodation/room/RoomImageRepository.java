package com.mungnyang.repository.product.accommodation.room;

import com.mungnyang.entity.product.accommodation.room.RoomImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomImageRepository extends JpaRepository<RoomImage, Long> {
    List<RoomImage> findByRoomRoomIdOrderByRoomImageId(Long roomId);
}
