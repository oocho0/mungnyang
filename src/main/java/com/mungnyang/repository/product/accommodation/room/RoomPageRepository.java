package com.mungnyang.repository.product.accommodation.room;

import com.mungnyang.dto.product.accommodation.room.ListRoomDto;

import java.util.List;

public interface RoomPageRepository {
    List<ListRoomDto> findListRoomDtoByAccommodationId(Long accommodationId);
}
