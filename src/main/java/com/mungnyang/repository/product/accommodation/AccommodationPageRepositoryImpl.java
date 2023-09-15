package com.mungnyang.repository.product.accommodation;

import com.mungnyang.constant.IsTrue;
import com.mungnyang.constant.Status;
import com.mungnyang.dto.product.QResultDto;
import com.mungnyang.dto.product.QTopInfoDto;
import com.mungnyang.dto.product.ResultDto;
import com.mungnyang.dto.product.TopInfoDto;
import com.mungnyang.dto.product.accommodation.ListAccommodationDto;
import com.mungnyang.dto.product.accommodation.QListAccommodationDto;
import com.mungnyang.entity.fixedEntity.QCity;
import com.mungnyang.entity.fixedEntity.QState;
import com.mungnyang.entity.product.accommodation.QAccommodation;
import com.mungnyang.entity.product.accommodation.QAccommodationComment;
import com.mungnyang.entity.product.accommodation.QAccommodationImage;
import com.mungnyang.entity.product.accommodation.room.QRoom;
import com.mungnyang.entity.service.QReservationRoom;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class AccommodationPageRepositoryImpl implements AccommodationPageRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private final QAccommodation accommodation = QAccommodation.accommodation;
    private final QAccommodationComment accommodationComment = QAccommodationComment.accommodationComment;
    private final QState state = QState.state;
    private final QCity city = QCity.city;
    private final QAccommodationImage accommodationImage = QAccommodationImage.accommodationImage;
    private final QRoom room = QRoom.room;
    private final QReservationRoom reservationRoom = QReservationRoom.reservationRoom;

    private BooleanExpression searchByCheckInTime(LocalDateTime checkInTime, LocalDateTime checkOutDate) {
        if (checkInTime == null || checkOutDate == null) {
            return null;
        }
        return room.roomId.notIn(
                jpaQueryFactory.select(room.roomId)
                        .from(room)
                        .leftJoin(reservationRoom).on(room.roomId.eq(reservationRoom.room.roomId))
                        .where(reservationRoom.checkInDate.eq(checkInTime)
                                .or(reservationRoom.checkOutDate.eq(checkOutDate))
                                .or(reservationRoom.checkInDate.between(checkInTime, checkOutDate))
                                .or(reservationRoom.checkOutDate.between(checkInTime, checkOutDate))
                                .or(reservationRoom.checkInDate.before(checkInTime).and(reservationRoom.checkOutDate.after(checkInTime)))
                                .or(reservationRoom.checkInDate.before(checkOutDate).and(reservationRoom.checkOutDate.after(checkOutDate)))));
    }

    @Override
    public List<ListAccommodationDto> findListAccommodationDtoByCreatedByAndIsNotDeleteOrderByReqDateDesc(String email) {
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
                .where(accommodation.createdBy.eq(email),
                        accommodation.accommodationStatus.ne(Status.CLOSED))
                .orderBy(accommodation.reqDate.desc())
                .groupBy(accommodation.accommodationId)
                .fetch();
    }

    @Override
    public List<TopInfoDto> getAccommodationTopListBySmallCategory(Long smallCategoryId) {
        return jpaQueryFactory.select(
                        new QTopInfoDto(
                                accommodation.accommodationId,
                                accommodation.accommodationName,
                                accommodation.city.state.name.concat(" ").concat(accommodation.city.name),
                                accommodationComment.comment.rate.avg().coalesce(0.0).floatValue(),
                                accommodationComment.count(),
                                accommodationImage.image.url
                        )
                ).from(accommodation)
                .leftJoin(accommodationComment).on(accommodation.accommodationId.eq(accommodationComment.accommodation.accommodationId))
                .leftJoin(accommodationImage).on(accommodation.accommodationId.eq(accommodationImage.accommodation.accommodationId))
                .where(accommodation.smallCategory.smallCategoryId.eq(smallCategoryId),
                        accommodationImage.image.isRepresentative.eq(IsTrue.YES),
                        accommodation.accommodationStatus.ne(Status.CLOSED))
                .groupBy(accommodation.accommodationId, accommodationImage.image.url)
                .orderBy(accommodationComment.comment.rate.avg().desc()).limit(3)
                .fetch();
    }

    @Override
    public List<ResultDto> getAccommodationResultsByFilter(List<Long> categoryId, List<Long> cityId, Integer headCount,
                                                           LocalDateTime checkInDate, LocalDateTime checkOutDate) {
        return jpaQueryFactory.select(
                        new QResultDto(
                                accommodation.accommodationId,
                                accommodation.accommodationName,
                                accommodation.smallCategory.bigCategory.name.concat("/").concat(accommodation.smallCategory.name),
                                accommodationComment.comment.rate.avg().coalesce(0.0).floatValue(),
                                accommodationComment.count(),
                                accommodation.accommodationStatus.stringValue(),
                                accommodation.productAddress.address.main,
                                accommodation.productAddress.Lon,
                                accommodation.productAddress.Lat,
                                accommodationImage.image.url
                        )
                ).from(accommodation)
                .leftJoin(accommodationComment).on(accommodation.accommodationId.eq(accommodationComment.accommodation.accommodationId))
                .leftJoin(room).on(accommodation.accommodationId.eq(room.accommodation.accommodationId))
                .leftJoin(accommodationImage).on(accommodation.accommodationId.eq(accommodationImage.accommodation.accommodationId))
                .where(accommodation.smallCategory.smallCategoryId.in(categoryId),
                        accommodation.city.cityId.in(cityId),
                        room.headCount.goe(headCount),
                        searchByCheckInTime(checkInDate, checkOutDate),
                        accommodationImage.image.isRepresentative.eq(IsTrue.YES),
                        accommodation.accommodationStatus.ne(Status.CLOSED))
                .groupBy(accommodation.accommodationId, accommodationImage.image.url)
                .orderBy(accommodationComment.comment.rate.avg().coalesce(0.0).floatValue().desc())
                .fetch();
    }
}
