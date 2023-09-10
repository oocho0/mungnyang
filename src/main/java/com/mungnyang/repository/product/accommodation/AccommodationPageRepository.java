package com.mungnyang.repository.product.accommodation;

import com.mungnyang.dto.product.TopInfoDto;
import com.mungnyang.dto.product.accommodation.ListAccommodationDto;

import java.util.List;


public interface AccommodationPageRepository {
    List<ListAccommodationDto> findListAccommodationDtoByCreatedByAndIsNotDeleteOrderByReqDateDesc(String email);

    List<TopInfoDto> getAccommodationTopListBySmallCategory(Long smallCategoryId);
}
