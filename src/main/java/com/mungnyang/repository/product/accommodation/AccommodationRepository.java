package com.mungnyang.repository.product.accommodation;

import com.mungnyang.entity.product.accommodation.Accommodation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {

    Accommodation findByAccommodationName(String accommodationName);
}
