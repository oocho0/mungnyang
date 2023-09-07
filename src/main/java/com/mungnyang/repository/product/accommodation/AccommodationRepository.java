package com.mungnyang.repository.product.accommodation;

import com.mungnyang.constant.Status;
import com.mungnyang.entity.product.accommodation.Accommodation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface AccommodationRepository extends JpaRepository<Accommodation, Long>, QuerydslPredicateExecutor<Accommodation>, AccommodationPageRepository {

    Accommodation findByAccommodationIdAndAccommodationStatusNot(Long accommodationIs, Status closed);
    Accommodation findByAccommodationNameAndAccommodationStatusNot(String accommodationName, Status closed);
}
