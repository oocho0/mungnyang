package com.mungnyang.repository.accommodation;

import com.mungnyang.entity.accommodation.AccommodationComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccommodationCommentRepository extends JpaRepository<AccommodationComment, Long> {
}
