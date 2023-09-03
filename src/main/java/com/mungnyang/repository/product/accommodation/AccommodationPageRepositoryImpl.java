package com.mungnyang.repository.product.accommodation;

import com.mungnyang.dto.product.accommodation.ListAccommodationDto;
import com.mungnyang.dto.product.accommodation.QListAccommodationDto;
import com.mungnyang.dto.product.accommodation.room.ListRoomDto;
import com.mungnyang.entity.fixedEntity.QCity;
import com.mungnyang.entity.fixedEntity.QState;
import com.mungnyang.entity.product.accommodation.QAccommodation;
import com.mungnyang.entity.product.accommodation.QAccommodationComment;
import com.mungnyang.entity.product.accommodation.room.QRoom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

@Repository
@RequiredArgsConstructor
public class AccommodationPageRepositoryImpl implements AccommodationPageRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<ListAccommodationDto> findListAccommodationDtoByCreatedByOrderByReqDateDesc(String email) {
        QAccommodation accommodation = QAccommodation.accommodation;
        QAccommodationComment accommodationComment = QAccommodationComment.accommodationComment;
        QState state = QState.state;
        QCity city = QCity.city;

        return jpaQueryFactory.select(
                        new QListAccommodationDto(
                                accommodation.accommodationId,
                                accommodation.accommodationName,
                                state.name,
                                city.name,
                                accommodation.accommodationStatus.stringValue(),
                                accommodationComment.comment.rate.avg().coalesce(0.0).floatValue(),
                                accommodationComment.count()
                        )
                )
                .from(accommodation)
                .join(accommodation.city, city)
                .join(city.state, state)
                .leftJoin(accommodationComment).on(accommodation.accommodationId.eq(accommodationComment.accommodation.accommodationId))
                .where(accommodation.createdBy.eq(email))
                .orderBy(accommodation.reqDate.desc())
                .groupBy(accommodation.accommodationId)
                .fetch();
    }


    public List<ListAccommodationDto> findListAccommodationDtoByCreatedByOrderByReqDateDescwithRoom(String email) {
        QAccommodation accommodation = QAccommodation.accommodation;
        QAccommodationComment accommodationComment = QAccommodationComment.accommodationComment;
        QState state = QState.state;
        QCity city = QCity.city;
        QRoom room = QRoom.room;

        return jpaQueryFactory.selectFrom(accommodation)
                .join(accommodation.city, city)
                .join(city.state, state)
                .leftJoin(accommodationComment).on(accommodation.accommodationId.eq(accommodationComment.accommodation.accommodationId))
                .leftJoin(room).on(accommodation.accommodationId.eq(room.accommodation.accommodationId))
                .where(QAccommodation.accommodation.createdBy.eq(email))
                .orderBy(accommodation.reqDate.desc())
                .groupBy(accommodation.accommodationId, room.roomId)
                .transform(
                        groupBy(accommodation.accommodationId).list(
                                Projections.constructor(
                                        ListAccommodationDto.class,
                                        accommodation.accommodationId,
                                        accommodation.accommodationName,
                                        state.name,
                                        city.name,
                                        accommodation.accommodationStatus.stringValue(),
                                        accommodationComment.comment.rate.avg().coalesce(0.0).floatValue(),
                                        accommodationComment.count(),
                                        list(
                                                Projections.constructor(
                                                        ListRoomDto.class,
                                                        room.roomId,
                                                        room.roomName,
                                                        room.roomPrice,
                                                        room.roomStatus.stringValue()
                                                )
                                        ).as("rooms")
                                )
                        )
                );
    }
}
