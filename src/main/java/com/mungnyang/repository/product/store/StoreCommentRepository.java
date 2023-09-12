package com.mungnyang.repository.product.store;

import com.mungnyang.entity.product.store.StoreComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StoreCommentRepository extends JpaRepository<StoreComment, Long>, QuerydslPredicateExecutor<StoreComment>, StoreCommentPageRepository {

    List<StoreComment> findByStoreStoreIdOrderByStoreCommentId(Long storeId);
    Long countByStoreStoreId(Long storeId);
}
