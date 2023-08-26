package com.mungnyang.repository.accommodation;

import com.mungnyang.entity.accommodation.Accommodation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {
}
