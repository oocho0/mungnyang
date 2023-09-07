package com.mungnyang.repository.product.accommodation;

import com.mungnyang.constant.IsTrue;
import com.mungnyang.constant.Status;
import com.mungnyang.dto.product.accommodation.ListAccommodationDto;
import com.mungnyang.dto.product.accommodation.QListAccommodationDto;
import com.mungnyang.entity.fixedEntity.QCity;
import com.mungnyang.entity.fixedEntity.QState;
import com.mungnyang.entity.product.accommodation.QAccommodation;
import com.mungnyang.entity.product.accommodation.QAccommodationComment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AccommodationPageRepositoryImpl implements AccommodationPageRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<ListAccommodationDto> findListAccommodationDtoByCreatedByAndIsNotDeleteOrderByReqDateDesc(String email) {
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
                .where(accommodation.createdBy.eq(email),
                        accommodation.accommodationStatus.ne(Status.CLOSED))
                .orderBy(accommodation.reqDate.desc())
                .groupBy(accommodation.accommodationId)
                .fetch();
    }
}
