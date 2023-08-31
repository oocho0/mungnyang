package com.mungnyang.repository.product.accommodation;

import com.mungnyang.entity.product.accommodation.AccommodationFacility;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccommodationFacilityRepository extends JpaRepository<AccommodationFacility, Long> {
    List<AccommodationFacility> findByAccommodationAccommodationIdOrderByAccommodationFacilityId(Long accommodationId);
}
