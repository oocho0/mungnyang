package com.mungnyang.repository.fixedEntity;

import com.mungnyang.dto.fixedEntityDto.MainBigCategoryDto;
import com.mungnyang.entity.fixedEntity.BigCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BigCategoryRepository extends JpaRepository<BigCategory, Long> {

    List<BigCategory> findAllByOrderByBigCategoryIdAsc();

    List<BigCategory> findByBigCategoryIdNotOrderByBigCategoryIdAsc(Long bigCategoryId);

    @Query("select new com.mungnyang.dto.fixedEntityDto.MainBigCategoryDto(bc.bigCategoryId, bc.name, count(s.storeId))" +
            " from BigCategory bc" +
            " left join SmallCategory sc on bc.bigCategoryId = sc.bigCategory.bigCategoryId" +
            " left join Store s on sc.smallCategoryId = s.smallCategory.smallCategoryId" +
            " where bc.bigCategoryId != :bigCategoryId" +
            " group by bc.bigCategoryId")
    List<MainBigCategoryDto> findMainBigCategoryDtoAllForStore(@Param("bigCategoryId") Long bigCategoryId);
}

