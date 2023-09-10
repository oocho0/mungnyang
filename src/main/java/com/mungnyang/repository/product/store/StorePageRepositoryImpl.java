package com.mungnyang.repository.product.store;

import com.mungnyang.constant.IsTrue;
import com.mungnyang.constant.Status;
import com.mungnyang.dto.product.QTopInfoDto;
import com.mungnyang.dto.product.SearchStoreFilter;
import com.mungnyang.dto.product.TopInfoDto;
import com.mungnyang.dto.product.store.ListStoreDto;
import com.mungnyang.dto.product.store.QListStoreDto;
import com.mungnyang.dto.product.store.QResultStoreDto;
import com.mungnyang.dto.product.store.ResultStoreDto;
import com.mungnyang.entity.fixedEntity.QBigCategory;
import com.mungnyang.entity.fixedEntity.QCity;
import com.mungnyang.entity.fixedEntity.QSmallCategory;
import com.mungnyang.entity.fixedEntity.QState;
import com.mungnyang.entity.product.store.QStore;
import com.mungnyang.entity.product.store.QStoreComment;
import com.mungnyang.entity.product.store.QStoreImage;
import com.mungnyang.service.product.StatusService;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.thymeleaf.util.StringUtils;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class StorePageRepositoryImpl implements StorePageRepository {

    private final JPAQueryFactory jpaQueryFactory;

    private final QStore store = QStore.store;
    private final QCity city = QCity.city;
    private final QState state = QState.state;
    private final QSmallCategory smallCategory = QSmallCategory.smallCategory;
    private final QBigCategory bigCategory = QBigCategory.bigCategory;
    private final QStoreComment storeComment = QStoreComment.storeComment;
    private final QStoreImage storeImage = QStoreImage.storeImage;

    private BooleanExpression searchByStoreStatus(Status storeStatus) {
        return storeStatus == null ? null : store.storeStatus.eq(storeStatus);
    }

    private BooleanExpression searchByCityId(Long cityId) {
        return cityId == null ? null : store.city.cityId.eq(cityId);
    }

    private BooleanExpression searchByState(Long stateId) {
        return stateId == null ? null : store.city.state.stateId.eq(stateId);
    }

    private BooleanExpression searchBySmallCategoryId(Long smallCategoryId) {
        return smallCategoryId == null ? null : store.smallCategory.smallCategoryId.eq(smallCategoryId);
    }

    private BooleanExpression searchByBigCategoryId(Long bigCategoryId) {
        return bigCategoryId == null ? null : store.smallCategory.bigCategory.bigCategoryId.eq(bigCategoryId);
    }

    private BooleanExpression searchByStoreName(String storeName) {
        return StringUtils.isEmpty(storeName) ? null : store.storeName.like("%" + storeName + "%");
    }

    @Override
    public Page<ListStoreDto> getStoreListDtoCriteriaPaging(SearchStoreFilter searchStoreFilter, Pageable pageable) {
        Status storeStatus = (searchStoreFilter.getByStoreStatus() == null || searchStoreFilter.getByStoreStatus().equals("")) ? null : StatusService.statusConverter(searchStoreFilter.getByStoreStatus());
        List<ListStoreDto> results = jpaQueryFactory.select(
                        new QListStoreDto(
                                store.storeId,
                                store.storeName,
                                bigCategory.name,
                                smallCategory.name,
                                state.name,
                                city.name,
                                store.storeStatus.stringValue(),
                                storeComment.comment.rate.avg().coalesce(0.0).floatValue(),
                                storeComment.count()
                        )
                )
                .from(store)
                .join(store.city, city)
                .join(city.state, state)
                .join(store.smallCategory, smallCategory)
                .join(smallCategory.bigCategory, bigCategory)
                .leftJoin(storeComment).on(store.storeId.eq(storeComment.store.storeId))
                .where(searchByStoreStatus(storeStatus),
                        searchByCityId(searchStoreFilter.getByCity()),
                        searchByState(searchStoreFilter.getByState()),
                        searchBySmallCategoryId(searchStoreFilter.getBySmallCategory()),
                        searchByBigCategoryId(searchStoreFilter.getByBigCategory()),
                        searchByStoreName(searchStoreFilter.getByStoreName()))
                .orderBy(store.storeId.desc())
                .groupBy(store.storeId)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> counts = jpaQueryFactory.select(store.count())
                .from(store)
                .join(store.city, city)
                .join(store.city.state, state)
                .join(store.smallCategory, smallCategory)
                .join(store.smallCategory.bigCategory, bigCategory)
                .where(searchByStoreStatus(storeStatus),
                        searchByCityId(searchStoreFilter.getByCity()),
                        searchByState(searchStoreFilter.getByState()),
                        searchBySmallCategoryId(searchStoreFilter.getBySmallCategory()),
                        searchByBigCategoryId(searchStoreFilter.getByBigCategory()),
                        searchByStoreName(searchStoreFilter.getByStoreName())
                );

        return PageableExecutionUtils.getPage(results, pageable, counts::fetchOne);
    }

    @Override
    public List<TopInfoDto> getStoreTopListByBigCategory(Long bigCategoryId) {
        return jpaQueryFactory.select(
                        new QTopInfoDto(
                                store.storeName,
                                store.city.state.name.concat(" ").concat(store.city.name),
                                storeComment.comment.rate.avg().coalesce(0.0).floatValue(),
                                storeComment.count(),
                                storeImage.image.url
                        )
                ).from(store)
                .leftJoin(storeComment).on(store.storeId.eq(storeComment.store.storeId))
                .leftJoin(storeImage).on(store.storeId.eq(storeImage.store.storeId))
                .where(store.smallCategory.bigCategory.bigCategoryId.eq(bigCategoryId),
                        storeImage.image.isRepresentative.eq(IsTrue.YES))
                .groupBy(store.storeId, storeImage.image.url)
                .orderBy(storeComment.comment.rate.avg().desc()).limit(3)
                .fetch();
    }

    @Override
    public List<ResultStoreDto> getStoreResultsByFilters(List<Long> smallCategoryId, List<Long> cityId) {
        return jpaQueryFactory.select(
                        new QResultStoreDto(
                                store.storeId,
                                store.storeName,
                                store.smallCategory.bigCategory.name.concat(" / ").concat(store.smallCategory.name),
                                storeComment.comment.rate.avg().coalesce(0.0).floatValue(),
                                storeComment.count(),
                                store.productAddress.address.main,
                                store.productAddress.Lon,
                                store.productAddress.Lat
                        )
                ).from(store)
                .leftJoin(storeComment).on(store.storeId.eq(storeComment.store.storeId))
                .where(store.smallCategory.smallCategoryId.in(smallCategoryId))
                .where(store.city.cityId.in(cityId))
                .groupBy(store.storeId)
                .orderBy(storeComment.comment.rate.avg().desc())
                .fetch();
    }


}
