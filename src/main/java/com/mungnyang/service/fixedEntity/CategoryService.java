package com.mungnyang.service.fixedEntity;

import com.mungnyang.entity.fixedEntity.BigCategory;
import com.mungnyang.entity.fixedEntity.SmallCategory;
import com.mungnyang.repository.fixedEntity.BigCategoryRepository;
import com.mungnyang.repository.fixedEntity.SmallCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final BigCategoryRepository bigCategoryRepository;
    private final SmallCategoryRepository smallCategoryRepository;

    public List<BigCategory> getAllBigCategories() {
        return bigCategoryRepository.findAllByOrderByBigCategoryIdAsc();
    }

    public List<SmallCategory> getSmallCategoriesByBigCategoryId(Long bigCategoryId){
        return smallCategoryRepository.findByBigCategoryBigCategoryIdOrderBySmallCategoryIdAsc(bigCategoryId);
    }

}
