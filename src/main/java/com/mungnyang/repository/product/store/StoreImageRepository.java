package com.mungnyang.repository.product.store;

import com.mungnyang.entity.product.store.StoreImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreImageRepository extends JpaRepository<StoreImage, Long> {

    List<StoreImage> findByStoreStoreIdOrderByStoreImageId(Long storeId);

}
