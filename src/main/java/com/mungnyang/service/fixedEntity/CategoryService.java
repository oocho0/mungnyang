package com.mungnyang.service.fixedEntity;

import com.mungnyang.constant.Status;
import com.mungnyang.dto.fixedEntityDto.MainBigCategoryDto;
import com.mungnyang.dto.fixedEntityDto.MainSmallCategoryDto;
import com.mungnyang.entity.fixedEntity.BigCategory;
import com.mungnyang.entity.fixedEntity.SmallCategory;
import com.mungnyang.entity.product.Product;
import com.mungnyang.entity.product.store.Store;
import com.mungnyang.repository.fixedEntity.BigCategoryRepository;
import com.mungnyang.repository.fixedEntity.SmallCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {
    private final BigCategoryRepository bigCategoryRepository;
    private final SmallCategoryRepository smallCategoryRepository;

    /**
     * 조회 화면에서의 숙소를 제외한 대분류 리스트 반환
     * @return 대분류의 정보와 대분류 별 편의 시설 갯수를 가진 Dto 리스트
     */
    public List<MainBigCategoryDto> getMainBigCategoryDtoForStore(){
        return bigCategoryRepository.findMainBigCategoryDtoAllForStore(1L);
    }

    public List<MainSmallCategoryDto> getMainSmallCategoryDtoByBigCategory(Long accommodationId){
        if (accommodationId == 1L) {
            return smallCategoryRepository.findMainSmallCategoryDtoListForAccommodation(accommodationId, Status.CLOSED);
        }
        return smallCategoryRepository.findMainSmallCategoryDtoListForStore(accommodationId);
    }

    /**
     * 대분류 big_category_id로 소분류 조회해서 리스트로 반환
     * @param bigCategoryId 조회할 대분류 id
     * @return
     */
    public List<SmallCategory> getSmallCategoryListByBigCategoryId(Long bigCategoryId){
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
