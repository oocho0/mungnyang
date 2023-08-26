package com.mungnyang.repository.store;

import com.mungnyang.entity.store.StoreComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreCommentRepository extends JpaRepository<StoreComment, Long> {
}
