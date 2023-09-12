package com.mungnyang.repository.product.store;

import com.mungnyang.dto.product.CommentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StoreCommentPageRepository {
    Page<CommentDto> getStoreCommentDtoPaging(Long storeId, Pageable pageable);

    Float getRateAverage(Long storeId);
}
