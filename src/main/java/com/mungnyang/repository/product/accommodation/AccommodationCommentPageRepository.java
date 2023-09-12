package com.mungnyang.repository.product.accommodation;

import com.mungnyang.dto.product.CommentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AccommodationCommentPageRepository {
    Page<CommentDto> getAccommodationCommentDtoPaging(Long accommodationId, Pageable pageable);

    Float getRateAverage(Long accommodationId);
}
