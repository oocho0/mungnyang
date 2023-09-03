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

    /**
     * 모든 대분류를 Big_category_id의 오름차순으로 정렬 조회
     * @return
     */
    public List<BigCategory> getAllBigCategories() {
        return bigCategoryRepository.findAllByOrderByBigCategoryIdAsc();
    }

    public BigCategory getBigCategoriesById(Long bigCategoryId) {
        return bigCategoryRepository.findById(bigCategoryId).orElseThrow(IllegalArgumentException::new);
    }

    /**
     * 대분류 big_category_id로 소분류 조회해서 리스트로 반환
     * @param bigCategoryId 조회할 대분류 id
     * @return
     */
    public List<SmallCategory> getSmallCategoriesWithOutThisBigCategoryId(Long bigCategoryId){
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

    /**
     * 편의 시설 등록 시 필요한 대분류 리스트 전달
     */
    public List<BigCategory> getBigCategoriesForStore() {
        return bigCategoryRepository.findByBigCategoryIdNotOrderByBigCategoryIdAsc(1L);
    }

}
