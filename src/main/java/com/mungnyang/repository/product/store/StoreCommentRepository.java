package com.mungnyang.repository.product.store;

import com.mungnyang.entity.product.store.StoreComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StoreCommentRepository extends JpaRepository<StoreComment, Long> {

    @Query("select count(sc) from StoreComment sc where sc.store.storeId = :storeId")
    Long countStoreCommentByStore(@Param("storeId") Long storeId);

    Float findRateByStoreId(Long storeId);
}
