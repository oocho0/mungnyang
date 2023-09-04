package com.mungnyang.service.product.store;

import com.mungnyang.constant.Status;
import com.mungnyang.dto.product.ModifyImageDto;
import com.mungnyang.dto.product.SearchStoreFilter;
import com.mungnyang.dto.product.store.CreateStoreDto;
import com.mungnyang.dto.product.store.ListStoreDto;
import com.mungnyang.dto.product.store.ModifyStoreDto;
import com.mungnyang.entity.fixedEntity.BigCategory;
import com.mungnyang.entity.fixedEntity.City;
import com.mungnyang.entity.fixedEntity.SmallCategory;
import com.mungnyang.entity.fixedEntity.State;
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
import org.springframework.ui.Model;
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
     * 편의 시설 수정 화면 초기화
     * @param model 편의 시설 수정 화면에 렌더링할 Model
     * @param modifyStoreDto 화면에 나타낼 편의 시설 정보
     */
    public void initializeModifyStorePage(Model model, ModifyStoreDto modifyStoreDto) {
        List<BigCategory> bigCategories = categoryService.getBigCategoriesForStore();
        model.addAttribute("bigCategories", bigCategories);
        List<SmallCategory> smallCategories = categoryService.getSmallCategoryListByBigCategoryId(modifyStoreDto.getSmallCategoryBigCategoryBigCategoryId());
        model.addAttribute("smallCategories", smallCategories);
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
    public Page<ListStoreDto> findStoresByConditionsAndPage(SearchStoreFilter searchStoreFilter, Pageable pageable) {
        return storeRepository.getStoreListDtoCriteriaPaging(searchStoreFilter, pageable);
    }

    /**
     * 수정할 Store의 정보 가져오기
     * @param storeId 수정할 Store의 일련번호
     * @return 수정할 Store의 정보
     */
    @Transactional(readOnly = true)
    public ModifyStoreDto getModifyStoreDtoByStoreId(Long storeId) {
        Store findStore = getStoreByStoreId(storeId);
        modelMapper.typeMap(Store.class, ModifyStoreDto.class).addMappings(mapping -> {
            mapping.using((Converter<Status, String>) ctx -> StatusService.statusConverter(ctx.getSource())).map(Store::getStoreStatus, ModifyStoreDto::setStoreStatus);
        });
        return modelMapper.map(findStore, ModifyStoreDto.class);
    }

    /**
     * 편의 시설 정보 수정
     * @param storeId 수정할 편의 시설의 일련번호
     * @param modifyStoreDto 수정할 편의 시설의 정보
     * @param imageDtoList 수정할 편의 시설의 이미지 리스트
     */
    public void updateStore(Long storeId, ModifyStoreDto modifyStoreDto, List<ModifyImageDto> imageDtoList) throws Exception {
        Store savedStore = getStoreByStoreId(storeId);
        modelMapper.typeMap(ModifyStoreDto.class, Store.class).addMappings(mapping -> {
            mapping.using((Converter<String , Status>) ctx -> StatusService.statusConverter(ctx.getSource())).map(ModifyStoreDto::getStoreStatus, Store::setStoreStatus);
            mapping.skip(Store::setStoreId);
            mapping.skip(Store::setSmallCategory);
            mapping.skip(Store::setCity);
        });
        modelMapper.map(modifyStoreDto, savedStore);
        savedStore.setSmallCategory(categoryService.getSmallCategoryBySmallCategoryId(modifyStoreDto.getSmallCategorySmallCategoryId()));
        savedStore.setCity(stateCityService.getMatchedCity(modifyStoreDto.getProductAddressAddressZipcode()));
        storeImageService.updateStoreImages(savedStore, imageDtoList);
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
     * StoreId로 Store 찾기
     * @param storeId 찾을 Store의 Id
     * @return Store가 없으면 예외 반환
     */
    private Store getStoreByStoreId(Long storeId) {
        return storeRepository.findById(storeId).orElseThrow(IllegalArgumentException::new);
    }
}
