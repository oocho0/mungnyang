package com.mungnyang.repository.product.store;

import com.mungnyang.constant.Status;
import com.mungnyang.dto.product.SearchStoreFilter;
import com.mungnyang.dto.product.store.QStoreListDto;
import com.mungnyang.dto.product.store.StoreListDto;
import com.mungnyang.entity.fixedEntity.QBigCategory;
import com.mungnyang.entity.fixedEntity.QCity;
import com.mungnyang.entity.fixedEntity.QSmallCategory;
import com.mungnyang.entity.fixedEntity.QState;
import com.mungnyang.entity.product.store.QStore;
import com.mungnyang.service.product.StatusService;
import com.querydsl.core.QueryResults;
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
public class StorePageRepositoryImpl implements StorePageRepository{

    private final JPAQueryFactory jpaQueryFactory;

    private BooleanExpression searchByStoreStatus(Status storeStatus) {
        return storeStatus == null ? null : QStore.store.storeStatus.eq(storeStatus);
    }

    private BooleanExpression searchByCityId(Long cityId) {
        return cityId == null ? null : QStore.store.city.cityId.eq(cityId);
    }

    private BooleanExpression searchByState(Long stateId) {
        return stateId == null ? null : QStore.store.city.state.stateId.eq(stateId);
    }

    private BooleanExpression searchBySmallCategoryId(Long smallCategoryId) {
        return smallCategoryId == null ? null : QStore.store.smallCategory.smallCategoryId.eq(smallCategoryId);
    }

    private BooleanExpression searchByBigCategoryId(Long bigCategoryId) {
        return bigCategoryId == null ? null : QStore.store.smallCategory.bigCategory.bigCategoryId.eq(bigCategoryId);
    }

    private BooleanExpression searchByStoreName(String storeName) {
        return StringUtils.isEmpty(storeName) ? null : QStore.store.storeName.like("%" + storeName + "%");
    }

    @Override
    public Page<StoreListDto> getStoreListDtoCriteriaPaging(SearchStoreFilter searchStoreFilter, Pageable pageable) {
        QStore store = QStore.store;
        QCity city = QCity.city;
        QState state = QState.state;
        QSmallCategory smallCategory = QSmallCategory.smallCategory;
        QBigCategory bigCategory = QBigCategory.bigCategory;

        Status storeStatus = StatusService.statusConverter(searchStoreFilter.getByStoreStatus());

        List<StoreListDto> results = jpaQueryFactory.select(
                        new QStoreListDto(store.storeId, store.storeName, bigCategory.name, smallCategory.name, state.name, city.name, store.storeStatus.stringValue(), null))
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
                        searchByStoreName(searchStoreFilter.getByStoreName()))
                .orderBy(store.storeId.desc())
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
}
