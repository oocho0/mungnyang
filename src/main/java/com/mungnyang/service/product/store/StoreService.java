package com.mungnyang.service.product.store;

import com.mungnyang.dto.product.store.CreateStoreDto;
import com.mungnyang.entity.fixedEntity.BigCategory;
import com.mungnyang.entity.product.store.Store;
import com.mungnyang.repository.product.store.StoreRepository;
import com.mungnyang.service.fixedEntity.CategoryService;
import com.mungnyang.service.fixedEntity.StateCityService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final StateCityService stateCityService;
    private final CategoryService categoryService;
    private final StoreImageService storeImageService;
    private final ModelMapper modelMapper;

    /**
     * 편의 시설 등록 시 필요한 대분류를 모델에 담아 전달
     * @param model 전달할 모델
     */
    public void initializeStore(Model model) {
        Long[] bigCategoryIds = {2L, 3L, 4L, 5L, 6L};
        List<BigCategory> bigCategories = categoryService.getBigCategoriesById(bigCategoryIds);
        model.addAttribute("bigCategories", bigCategories);
    }

    /**
     * 신규 편의 시설 등록하기
     * @param createStoreDto 페이지에 입력한 편의 시설 정보
     * @param storeImageFileList 페이지에 입력한 편의 시설 이미지들
     * @throws Exception
     */
    public void registerStore(CreateStoreDto createStoreDto, List<MultipartFile> storeImageFileList) throws Exception{
        Store store = createStore(createStoreDto);
        storeRepository.save(store);
        storeImageService.saveStoreImages(store, storeImageFileList);
    }

    /**
     * 편의 시설 생성
     * @param createStoreDto 페이지에 입력한 편의 시설체 정보
     * @return 생성된 편의 시설 객체
     */
    private Store createStore(CreateStoreDto createStoreDto) {
        modelMapper.typeMap(CreateStoreDto.class, Store.class).addMappings(mapping -> {
            mapping.skip(Store::setSmallCategory);
            mapping.skip(Store::setCity);
        });
        Store createdStore = modelMapper.map(createStoreDto, Store.class);
        createdStore.setSmallCategory(categoryService.getSmallCategoryBySmallCategoryId(createStoreDto.getSmallCategoryId()));
        createdStore.setCity(stateCityService.getMatchedCity(createStoreDto.getProductAddressAddressZipcode()));
        return createdStore;
    }

    /**
     * storeName으로 Store 찾기
     * @param name 찾을 Store의 이름
     * @return Null이면 예외 발생
     */
    public Store findStoreByName(String name) {
        Store findStore = storeRepository.findByStoreName(name);
        if (findStore == null) {
            throw new IllegalStateException();
        }
        return findStore;
    }


}
