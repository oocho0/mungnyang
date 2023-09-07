package com.mungnyang.repository.product.accommodation;

import com.mungnyang.dto.product.accommodation.ListAccommodationDto;

import java.util.List;


public interface AccommodationPageRepository {
    List<ListAccommodationDto> findListAccommodationDtoByCreatedByAndIsNotDeleteOrderByReqDateDesc(String email);
}
