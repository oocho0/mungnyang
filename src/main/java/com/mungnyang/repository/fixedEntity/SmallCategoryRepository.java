package com.mungnyang.repository.fixedEntity;

import com.mungnyang.entity.fixedEntity.SmallCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SmallCategoryRepository extends JpaRepository<SmallCategory, Long> {

    List<SmallCategory> findByBigCategoryBigCategoryIdOrderBySmallCategoryIdAsc(Long bigCategoryId);
}
