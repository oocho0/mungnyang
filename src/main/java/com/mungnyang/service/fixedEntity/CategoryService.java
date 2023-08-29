package com.mungnyang.service.fixedEntity;

import com.mungnyang.entity.fixedEntity.BigCategory;
import com.mungnyang.entity.fixedEntity.SmallCategory;
import com.mungnyang.repository.fixedEntity.BigCategoryRepository;
import com.mungnyang.repository.fixedEntity.SmallCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final BigCategoryRepository bigCategoryRepository;
    private final SmallCategoryRepository smallCategoryRepository;

    /**
     * 모든 대분류를 Big_category_id의 오름차순으로 정렬 조회
     * @return
     */
    public List<BigCategory> getAllBigCategories() {
        return bigCategoryRepository.findAllByOrderByBigCategoryIdAsc();
    }

    public List<BigCategory> getBigCategoriesById(Long[] bigCategoryIds){
        List<BigCategory> bigCategories = new ArrayList<>();
        for (Long bigCategoryId : bigCategoryIds) {
            bigCategories.add(bigCategoryRepository.findById(bigCategoryId).orElseThrow(IllegalArgumentException::new));
        }
        return bigCategories;
    }

    public BigCategory getBigCategoriesById(Long bigCategoryId) {
        return bigCategoryRepository.findById(bigCategoryId).orElseThrow(IllegalArgumentException::new);
    }

    /**
     * 대분류 big_category_id로 소분류 조회해서 리스트로 반환
     * @param bigCategoryId 조회할 대분류 id
     * @return
     */
    public List<SmallCategory> getSmallCategoriesByBigCategoryId(Long bigCategoryId){
        return smallCategoryRepository.findByBigCategoryBigCategoryIdOrderBySmallCategoryIdAsc(bigCategoryId);
    }

    /**
     * 소분류 small_category_id로 소분류 조회
     * @param smallCategoryId
     * @return
     */
    public SmallCategory getSmallCategoryBySmallCategoryId(Long smallCategoryId){
        return smallCategoryRepository.findById(smallCategoryId).orElseThrow(IllegalStateException::new);
    }

}
