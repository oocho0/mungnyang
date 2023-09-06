package com.mungnyang.repository.product.accommodation;

import com.mungnyang.entity.product.accommodation.AccommodationComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccommodationCommentRepository extends JpaRepository<AccommodationComment, Long> {

    List<AccommodationComment> findByAccommodationAccommodationIdOrderByAccommodationCommentId(Long accommodationId);

}
