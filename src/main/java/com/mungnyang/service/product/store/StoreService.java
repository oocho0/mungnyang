package com.mungnyang.service.product.store;

import com.mungnyang.constant.Status;
import com.mungnyang.dto.product.*;
import com.mungnyang.dto.product.CommentDto;
import com.mungnyang.dto.product.store.CreateStoreDto;
import com.mungnyang.dto.product.store.DetailStoreDto;
import com.mungnyang.dto.product.store.ListStoreDto;
import com.mungnyang.dto.product.store.ModifyStoreDto;
import com.mungnyang.entity.Address;
import com.mungnyang.entity.fixedEntity.BigCategory;
import com.mungnyang.entity.fixedEntity.City;
import com.mungnyang.entity.fixedEntity.SmallCategory;
import com.mungnyang.entity.product.ProductAddress;
import com.mungnyang.entity.product.store.Store;
import com.mungnyang.entity.product.store.StoreComment;
import com.mungnyang.entity.product.store.StoreImage;
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
    private final StoreCommentService storeCommentService;
    private final ModelMapper modelMapper;

    /**
     * 신규 편의 시설 등록하기
     *
     * @param createStoreDto 페이지에 입력한 편의 시설 정보
     * @throws Exception
     */
    public void registerStore(CreateStoreDto createStoreDto) throws Exception {
        Store store = createStore(createStoreDto);
        storeRepository.save(store);
        storeImageService.saveStoreImages(store, createStoreDto.getImageList());
    }

    /**
     * 편의 시설 수정 화면 초기화
     *
     * @param model          편의 시설 수정 화면에 렌더링할 Model
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
     *
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
     *
     * @param searchStoreFilter 원하는 조건
     * @param pageable          해당 page
     * @return 페이지에 표시될 Store 정보 ListStoreDto의 페이징 객체
     */
    @Transactional(readOnly = true)
    public Page<ListStoreDto> findStoresByConditionsAndPage(SearchStoreFilter searchStoreFilter, Pageable pageable) {
        return storeRepository.getStoreListDtoCriteriaPaging(searchStoreFilter, pageable);
    }

    /**
     * 수정할 Store의 정보 가져오기
     *
     * @param storeId 수정할 Store의 일련번호
     * @return 수정할 Store의 ModifyStoreDTO
     */
    @Transactional(readOnly = true)
    public ModifyStoreDto getModifyStoreDtoByStoreId(Long storeId) {
        Store findStore = getStoreByStoreId(storeId);
        modelMapper.typeMap(Store.class, ModifyStoreDto.class).addMappings(mapping -> {
            mapping.using((Converter<Status, String>) ctx -> StatusService.statusConverter(ctx.getSource())).map(Store::getStoreStatus, ModifyStoreDto::setStoreStatus);
            mapping.skip(ModifyStoreDto::setImageList);
        });
        ModifyStoreDto modifyStoreDto = modelMapper.map(findStore, ModifyStoreDto.class);
        modifyStoreDto.setImageList(storeImageService.getModifyImageDtoListByStoreId(storeId));
        return modifyStoreDto;
    }

    /**
     * 편의 시설 정보 수정
     *
     * @param storeId        수정할 편의 시설의 일련번호
     * @param modifyStoreDto 수정할 편의 시설의 정보
     */
    public void updateStore(Long storeId, ModifyStoreDto modifyStoreDto) throws Exception {
        Store savedStore = getStoreByStoreId(storeId);
        savedStore.setStoreName(modifyStoreDto.getStoreName());
        savedStore.setSmallCategory(categoryService.getSmallCategoryBySmallCategoryId(modifyStoreDto.getSmallCategorySmallCategoryId()));
        City matchedCity = stateCityService.getMatchedCity(modifyStoreDto.getProductAddressAddressZipcode());
        savedStore.setCity(matchedCity);
        savedStore.setProductAddress(ProductAddress.builder()
                .address(Address.builder()
                        .zipcode(modifyStoreDto.getProductAddressAddressZipcode())
                        .main(modifyStoreDto.getProductAddressAddressMain())
                        .detail(modifyStoreDto.getProductAddressAddressDetail())
                        .extra(modifyStoreDto.getProductAddressAddressExtra())
                        .build())
                .Lon(modifyStoreDto.getProductAddressLon())
                .Lat(modifyStoreDto.getProductAddressLat())
                .build());
        savedStore.setStoreDetail(modifyStoreDto.getStoreDetail());
        savedStore.setStoreStatus(StatusService.statusConverter(modifyStoreDto.getStoreStatus()));
        storeImageService.updateStoreImages(savedStore, modifyStoreDto.getImageList());
    }

    /**
     * 메인 화면 TOP3에 나올 대분류별 편의 시설 리스트 반환
     *
     * @return MainTopDto 리스트
     */
    public List<MainTopDto> getStoresTopList() {
        List<MainTopDto> mainTopDtoList = new ArrayList<>();
        List<BigCategory> storeCategories = categoryService.getBigCategoriesForStore();
        for (BigCategory storeCategory : storeCategories) {
            List<TopInfoDto> topInfoDtoList = storeRepository.getStoreTopListByBigCategory(storeCategory.getBigCategoryId());
            mainTopDtoList.add(MainTopDto.builder()
                    .name(storeCategory.getName())
                    .info(topInfoDtoList)
                    .build());
        }
        return mainTopDtoList;
    }

    /**
     * 메인 화면 조회 결과 리스트 반환
     *
     * @param paramMap 메인 화면 조회 조건(소분류, 시/군/구)
     * @return 편의 시설 정보 ResultDto 리스트
     */
    public List<ResultDto> getStoreResultList(MultiParam paramMap) {
        List<Long> categoryId = paramMap.getCategoryId();
        List<Long> cityId = paramMap.getCityId();
        return storeRepository.getStoreResultsByFilters(categoryId, cityId);
    }

    /**
     * 편의 시설 자세한 정보 화면
     * @param storeId 해당 편의 시설 일련번호
     * @return DetailStoreDto
     */
    public DetailStoreDto getStoreDetails(Long storeId) {
        Store store = getStoreByStoreId(storeId);
        List<StoreImage> images = storeImageService.getStoreImageListByStoreId(storeId);
        List<String> imageList = new ArrayList<>();
        for (StoreImage image : images) {
            imageList.add(image.getImage().getUrl());
        }
        Page<CommentDto> commentList = storeCommentService.getCommentPage(storeId);
        return DetailStoreDto.builder()
                .id(store.getStoreId())
                .name(store.getStoreName())
                .category(store.getSmallCategory().getBigCategory().getName() + " / " + store.getSmallCategory().getName())
                .address("(" + store.getProductAddress().getAddress().getZipcode() + ") " + store.getProductAddress().getAddress().getMain() + " " + store.getProductAddress().getAddress().getDetail())
                .lat(store.getProductAddress().getLat())
                .lon(store.getProductAddress().getLon())
                .detail(store.getStoreDetail())
                .status(StatusService.statusConverter(store.getStoreStatus()))
                .rateAvg(storeCommentService.getRateAverage(storeId))
                .commentCount(storeCommentService.getCommentCount(storeId))
                .images(imageList)
                .comments(commentList)
                .build();
    }

    /**
     * 편의 시설 생성
     *
     * @param createStoreDto 페이지에 입력한 편의 시설체 정보
     * @return 생성된 Store 엔티티
     */
    private Store createStore(CreateStoreDto createStoreDto) {
        modelMapper.typeMap(CreateStoreDto.class, Store.class).addMappings(mapping -> {
            mapping.using((Converter<String, Status>) ctx -> StatusService.statusConverter(ctx.getSource())).map(CreateStoreDto::getStoreStatus, Store::setStoreStatus);
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
     *
     * @param storeId 찾을 Store의 Id
     * @return Store가 없으면 예외 반환
     */
    public Store getStoreByStoreId(Long storeId) {
        return storeRepository.findById(storeId).orElseThrow(IllegalArgumentException::new);
    }

    /**
     * 편의 시설 삭제 시 모든 정보 제거
     *
     * @param storeId 삭제할 편의 시설 일련번호
     * @throws Exception
     */
    public void deleteStore(Long storeId) throws Exception {
        Store savedStore = getStoreByStoreId(storeId);
        storeImageService.deleteAllStoreImage(savedStore);
        storeCommentService.deleteAllStoreComments(savedStore.getStoreId());
        storeRepository.delete(savedStore);
    }
}
