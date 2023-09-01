package com.mungnyang.repository.product.store;

import com.mungnyang.entity.product.store.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long>, QuerydslPredicateExecutor<Store>, StorePageRepository {

    Store findByStoreName(String storeName);

}
