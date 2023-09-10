package com.mungnyang.controller;

import com.mungnyang.dto.fixedEntityDto.MainBigCategoryDto;
import com.mungnyang.dto.fixedEntityDto.MainSmallCategoryDto;
import com.mungnyang.dto.fixedEntityDto.MainStateDto;
import com.mungnyang.dto.product.MainTopDto;
import com.mungnyang.dto.product.store.ResultStoreDto;
import com.mungnyang.entity.product.accommodation.Accommodation;
import com.mungnyang.entity.product.store.Store;
import com.mungnyang.service.fixedEntity.CategoryService;
import com.mungnyang.service.fixedEntity.StateCityService;
import com.mungnyang.service.product.accommodation.AccommodationService;
import com.mungnyang.service.product.store.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final CategoryService categoryService;
    private final StateCityService stateCityService;
    private final StoreService storeService;
    private final AccommodationService accommodationService;

    @GetMapping("/")
    public String main(Model model) {
        List<MainBigCategoryDto> bigCategoryDtoList = categoryService.getMainBigCategoryDtoForStore();
        model.addAttribute("bigCategories", bigCategoryDtoList);
        List<MainSmallCategoryDto> smallCategoryDtoList = categoryService.getMainSmallCategoryDtoByBigCategory(1L);
        model.addAttribute("smallCategories", smallCategoryDtoList);
        List<MainStateDto> storeStateDtoList = stateCityService.getMainStateDtoList(Store.class);
        model.addAttribute("storeState", storeStateDtoList);
        List<MainStateDto> accommodationStateList = stateCityService.getMainStateDtoList(Accommodation.class);
        model.addAttribute("accommodationState", accommodationStateList);
        List<MainTopDto> storeTopList = storeService.getStoresTopList();
        model.addAttribute("storeTopList", storeTopList);
        List<MainTopDto> accommodationTopList = accommodationService.getAccommodationsTopList();
        model.addAttribute("accommodationTopList", accommodationTopList);
        return "main";
    }

    @GetMapping("/search/store")
    public ResponseEntity<?> getStores(@RequestParam MultiValueMap<String, Long> paramMap){
        List<Long> categories = paramMap.get("category");
        List<Long> cities = paramMap.get("city");
        List<ResultStoreDto> resultStoreDtoList = storeService.getStoreResultList(categories, cities);
        return new ResponseEntity<List<ResultStoreDto>>(resultStoreDtoList, HttpStatus.OK);
    }
    
}
