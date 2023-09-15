package com.mungnyang.repository.product.accommodation;

import com.mungnyang.dto.product.ResultDto;
import com.mungnyang.dto.product.TopInfoDto;
import com.mungnyang.dto.product.accommodation.ListAccommodationDto;

import java.time.LocalDateTime;
import java.util.List;


public interface AccommodationPageRepository {
    List<ListAccommodationDto> findListAccommodationDtoByCreatedByAndIsNotDeleteOrderByReqDateDesc(String email);
    List<TopInfoDto> getAccommodationTopListBySmallCategory(Long smallCategoryId);
    List<ResultDto> getAccommodationResultsByFilter(List<Long> categoryId, List<Long> cityId, Integer headCount,
                                                    LocalDateTime checkInDate, LocalDateTime checkOutDate);
}
