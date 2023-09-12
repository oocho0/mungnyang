package com.mungnyang.repository.product.accommodation;

import com.mungnyang.dto.product.CommentDto;
import com.mungnyang.dto.product.QCommentDto;
import com.mungnyang.entity.product.accommodation.QAccommodationComment;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AccommodationCommentPageRepositoryImpl implements AccommodationCommentPageRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private final QAccommodationComment accommodationComment = QAccommodationComment.accommodationComment;

    @Override
    public Page<CommentDto> getAccommodationCommentDtoPaging(Long accommodationId, Pageable pageable) {
        List<CommentDto> results = jpaQueryFactory.select(
                        new QCommentDto(
                                accommodationComment.accommodationCommentId,
                                accommodationComment.comment.commentContent,
                                accommodationComment.comment.rate,
                                accommodationComment.createdBy,
                                accommodationComment.reqDate
                        )
                ).from(accommodationComment)
                .where(accommodationComment.accommodation.accommodationId.eq(accommodationId))
                .orderBy(accommodationComment.reqDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        JPAQuery<Long> counts = jpaQueryFactory.select(accommodationComment.count())
                .from(accommodationComment)
                .where(accommodationComment.accommodation.accommodationId.eq(accommodationId));
        return PageableExecutionUtils.getPage(results, pageable, counts::fetchOne);
    }

    @Override
    public Float getRateAverage(Long accommodationId) {
        return jpaQueryFactory.select(accommodationComment.comment.rate.avg().coalesce(0.0).floatValue())
                .from(accommodationComment)
                .where(accommodationComment.accommodation.accommodationId.eq(accommodationId))
                .groupBy(accommodationComment.accommodation.accommodationId)
                .fetchOne();
    }
}
