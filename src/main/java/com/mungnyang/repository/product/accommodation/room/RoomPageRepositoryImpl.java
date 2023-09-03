package com.mungnyang.repository.product.accommodation.room;

import com.mungnyang.dto.product.accommodation.room.ListRoomDto;
import com.mungnyang.dto.product.accommodation.room.QListRoomDto;
import com.mungnyang.entity.product.accommodation.room.QRoom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RoomPageRepositoryImpl implements RoomPageRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<ListRoomDto> findListRoomDtoByAccommodationId(Long accommodationId) {
        QRoom room = QRoom.room;
        return jpaQueryFactory.select(
                new QListRoomDto(
                        room.roomId,
                        room.roomName,
                        room.roomPrice,
                        room.roomStatus.stringValue()
                )
        ).from(room)
                .where(room.accommodation.accommodationId.eq(accommodationId))
                .orderBy(room.roomId.asc())
                .fetch();
    }
}
