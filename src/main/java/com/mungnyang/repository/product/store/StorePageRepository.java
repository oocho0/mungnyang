package com.mungnyang.repository.product.store;

import com.mungnyang.dto.product.SearchStoreFilter;
import com.mungnyang.dto.product.TopInfoDto;
import com.mungnyang.dto.product.store.ListStoreDto;
import com.mungnyang.dto.product.store.ResultStoreDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StorePageRepository {
    Page<ListStoreDto> getStoreListDtoCriteriaPaging(SearchStoreFilter searchStoreFilter, Pageable pageable);
    List<TopInfoDto> getStoreTopListByBigCategory(Long bigCategoryId);
    List<ResultStoreDto> getStoreResultsByFilters(List<Long> smallCategoryId, List<Long> cityId);
}
