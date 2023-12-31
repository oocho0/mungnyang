package com.mungnyang.repository.product.accommodation;

import com.mungnyang.constant.Status;
import com.mungnyang.entity.product.accommodation.Accommodation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface AccommodationRepository extends JpaRepository<Accommodation, Long>, QuerydslPredicateExecutor<Accommodation>, AccommodationPageRepository {

    Accommodation findByAccommodationIdAndAccommodationStatusNot(Long accommodationId, Status closed);
    Accommodation findByAccommodationNameAndAccommodationStatusNot(String accommodationName, Status closed);

    List<Accommodation> findByAccommodationStatus(Status closed);

    List<Accommodation> findByCreatedByAndAccommodationStatusNot(String email, Status closed);
}
