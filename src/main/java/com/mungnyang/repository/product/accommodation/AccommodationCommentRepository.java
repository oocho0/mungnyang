package com.mungnyang.repository.product.accommodation;

import com.mungnyang.entity.product.accommodation.AccommodationComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface AccommodationCommentRepository extends JpaRepository<AccommodationComment, Long>, QuerydslPredicateExecutor<AccommodationComment>, AccommodationCommentPageRepository {

    List<AccommodationComment> findByAccommodationAccommodationIdOrderByAccommodationCommentId(Long accommodationId);
    Long countByAccommodationAccommodationId(Long accommodationId);
}
