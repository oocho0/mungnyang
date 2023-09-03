package com.mungnyang.repository.product.accommodation;

import com.mungnyang.dto.product.accommodation.ListAccommodationDto;
import com.mungnyang.entity.product.accommodation.Accommodation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AccommodationRepository extends JpaRepository<Accommodation, Long>, QuerydslPredicateExecutor<Accommodation>, AccommodationPageRepository {

    Accommodation findByAccommodationName(String accommodationName);
}
