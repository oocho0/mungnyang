package com.mungnyang.repository.product.accommodation.room;

import com.mungnyang.entity.product.accommodation.room.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long>, QuerydslPredicateExecutor<Room>, RoomPageRepository {

    List<Room> findByAccommodationAccommodationIdOrderByRoomId(Long accommodationId);
}
