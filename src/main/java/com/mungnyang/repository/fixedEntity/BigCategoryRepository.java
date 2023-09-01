package com.mungnyang.repository.fixedEntity;

import com.mungnyang.entity.fixedEntity.BigCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BigCategoryRepository extends JpaRepository<BigCategory, Long> {

    List<BigCategory> findAllByOrderByBigCategoryIdAsc();

    List<BigCategory> findByBigCategoryIdNotOrderByBigCategoryIdAsc(Long bigCategoryId);
}

