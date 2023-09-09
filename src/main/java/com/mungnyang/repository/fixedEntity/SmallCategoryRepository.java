package com.mungnyang.repository.fixedEntity;

import com.mungnyang.constant.Status;
import com.mungnyang.dto.fixedEntityDto.MainSmallCategoryDto;
import com.mungnyang.entity.fixedEntity.SmallCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SmallCategoryRepository extends JpaRepository<SmallCategory, Long> {

    List<SmallCategory> findByBigCategoryBigCategoryIdOrderBySmallCategoryIdAsc(Long bigCategoryId);

    @Query("select new com.mungnyang.dto.fixedEntityDto.MainSmallCategoryDto(sc.smallCategoryId, sc.name, count(a.accommodationId))" +
            " from SmallCategory sc" +
            " left join Accommodation a on sc.smallCategoryId = a.smallCategory.smallCategoryId" +
            " where sc.bigCategory.bigCategoryId = :bigCategoryId and (a.accommodationStatus != :status or a.accommodationStatus is null)" +
            " group by sc.smallCategoryId")
    List<MainSmallCategoryDto> findMainSmallCategoryDtoListForAccommodation(@Param("bigCategoryId") Long bigCategoryId, @Param("status") Status closed);

    @Query("select new com.mungnyang.dto.fixedEntityDto.MainSmallCategoryDto(sc.smallCategoryId, sc.name, count(s.storeId))" +
            " from SmallCategory sc" +
            " left join Store s on sc.smallCategoryId = s.smallCategory.smallCategoryId" +
            " where sc.bigCategory.bigCategoryId = :bigCategoryId" +
            " group by sc.smallCategoryId")
    List<MainSmallCategoryDto> findMainSmallCategoryDtoListForStore(@Param("bigCategoryId") Long bigCategoryId);
}
