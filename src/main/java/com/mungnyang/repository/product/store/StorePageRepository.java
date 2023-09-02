package com.mungnyang.repository.product.store;

import com.mungnyang.dto.product.SearchStoreFilter;
import com.mungnyang.dto.product.store.ListStoreDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StorePageRepository {

    Page<ListStoreDto> getStoreListDtoCriteriaPaging(SearchStoreFilter searchStoreFilter, Pageable pageable);
}
