package com.mungnyang.repository.product.store;

import com.mungnyang.entity.product.store.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {
}
