package com.mungnyang.repository.product.accommodation;

import com.mungnyang.entity.product.accommodation.AccommodationImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccommodationImageRepository extends JpaRepository<AccommodationImage, Long> {
    List<AccommodationImage> findByAccommodationAccommodationIdOrderByAccommodationImageId(Long accommodationId);
}
