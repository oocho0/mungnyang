package com.mungnyang.service.product.store;

import com.mungnyang.constant.Status;
import com.mungnyang.dto.product.SearchStoreFilter;
import com.mungnyang.dto.product.store.CreateStoreDto;
import com.mungnyang.dto.product.store.StoreListDto;
import com.mungnyang.entity.product.store.Store;
import com.mungnyang.repository.product.store.StoreRepository;
import com.mungnyang.service.fixedEntity.CategoryService;
import com.mungnyang.service.fixedEntity.StateCityService;
import com.mungnyang.service.product.StatusService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final StateCityService stateCityService;
    private final CategoryService categoryService;
    private final StoreImageService storeImageService;
    private final StoreCommentService storeCommentService;
    private final ModelMapper modelMapper;

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
            mapping.using((Converter<String , Status>) ctx -> StatusService.statusConverter(ctx.getSource())).map(CreateStoreDto::getStoreStatus, Store::setStoreStatus);
            mapping.skip(Store::setStoreId);
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

    /**
     * 원하는 조건에 해당하는 Store의 정보를 page에 맞게 반환
     * @param searchStoreFilter 원하는 조건
     * @param pageable 해당 page
     * @return 페이지에 표시될 Store 정보
     */
    @Transactional(readOnly = true)
    public Page<StoreListDto> findStoresByConditionsAndPage(SearchStoreFilter searchStoreFilter, Pageable pageable) {
        Page<StoreListDto> storeListDtos = storeRepository.getStoreListDtoCriteriaPaging(searchStoreFilter, pageable);
        for (StoreListDto storeListDto : storeListDtos) {
            Long storeId = storeListDto.getStoreId();
            storeListDto.setCommentCount(storeCommentService.getStoreCommentCountByStoreId(storeId));
        }
        return storeListDtos;
    }

}
