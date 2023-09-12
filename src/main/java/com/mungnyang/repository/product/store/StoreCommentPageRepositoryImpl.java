package com.mungnyang.repository.product.store;

import com.mungnyang.dto.product.CommentDto;
import com.mungnyang.dto.product.QCommentDto;
import com.mungnyang.entity.product.store.QStoreComment;
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
public class StoreCommentPageRepositoryImpl implements StoreCommentPageRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private final QStoreComment storeComment = QStoreComment.storeComment;

    @Override
    public Page<CommentDto> getStoreCommentDtoPaging(Long storeId, Pageable pageable) {
        List<CommentDto> results = jpaQueryFactory.select(
                        new QCommentDto(
                                storeComment.storeCommentId,
                                storeComment.comment.commentContent,
                                storeComment.comment.rate,
                                storeComment.createdBy,
                                storeComment.reqDate
                        )
                ).from(storeComment)
                .where(storeComment.store.storeId.eq(storeId))
                .orderBy(storeComment.reqDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        JPAQuery<Long> counts = jpaQueryFactory.select(storeComment.count())
                .from(storeComment)
                .where(storeComment.store.storeId.eq(storeId));
        return PageableExecutionUtils.getPage(results, pageable, counts::fetchOne);
    }

    @Override
    public Float getRateAverage(Long storeId) {
        return jpaQueryFactory.select(storeComment.comment.rate.avg().coalesce(0.0).floatValue())
                .from(storeComment)
                .where(storeComment.store.storeId.eq(storeId))
                .groupBy(storeComment.store.storeId)
                .fetchOne();
    }
}
