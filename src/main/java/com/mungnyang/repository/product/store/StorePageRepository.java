package com.mungnyang.repository.product.store;

import com.mungnyang.dto.product.SearchStoreFilter;
import com.mungnyang.dto.product.store.StoreListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StorePageRepository {

    Page<StoreListDto> getStoreListDtoCriteriaPaging(SearchStoreFilter searchStoreFilter, Pageable pageable);
}
