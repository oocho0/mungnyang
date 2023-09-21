package com.mungnyang.controller;

import com.mungnyang.dto.fixedEntityDto.MainCityDto;
import com.mungnyang.dto.fixedEntityDto.MainSmallCategoryDto;
import com.mungnyang.dto.fixedEntityDto.MainStateDto;
import com.mungnyang.entity.fixedEntity.City;
import com.mungnyang.entity.fixedEntity.SmallCategory;
import com.mungnyang.entity.product.accommodation.Accommodation;
import com.mungnyang.entity.product.store.Store;
import com.mungnyang.service.fixedEntity.CategoryService;
import com.mungnyang.service.fixedEntity.StateCityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class FixedEntityController {
    private final StateCityService stateCityService;
    private final CategoryService categoryService;

    @GetMapping("/category/{bigCategoryId}")
    public ResponseEntity<?> findSmallCategoryIds(@PathVariable Long bigCategoryId) {
        List<SmallCategory> smallCategories = categoryService.getSmallCategoryListByBigCategoryId(bigCategoryId);
        return new ResponseEntity<List<SmallCategory>>(smallCategories, HttpStatus.OK);
    }

    @GetMapping("/state/{stateId}")
    public ResponseEntity<?> findCityIds(@PathVariable Long stateId) {
        List<City> cities = stateCityService.getCitiesByStateId(stateId);
        return new ResponseEntity<List<City>>(cities, HttpStatus.OK);
    }

    @GetMapping("/search/category/{bigCategoryId}")
    public ResponseEntity<?> findSmallCategoryStoreCount(@PathVariable Long bigCategoryId) {
        List<MainSmallCategoryDto> mainSmallCategoryDtoList = categoryService.getMainSmallCategoryDtoByBigCategory(bigCategoryId);
        return new ResponseEntity<List<MainSmallCategoryDto>>(mainSmallCategoryDtoList, HttpStatus.OK);
    }

    @GetMapping("/search/store/{stateId}")
    public ResponseEntity<?> findCityStoreCount(@PathVariable Long stateId, @RequestParam List<Long> smallCategoryId) {
        List<MainCityDto> mainCityDtoList = stateCityService.getMainCityDtoList(Store.class, stateId, smallCategoryId);
        return new ResponseEntity<List<MainCityDto>>(mainCityDtoList, HttpStatus.OK);
    }

    @GetMapping("/search/accommodation/{stateId}")
    public ResponseEntity<?> findCityAccommodationCount(@PathVariable Long stateId, @RequestParam List<Long> accommodationCategoryId) {
        List<MainCityDto> mainCityDtoList = stateCityService.getMainCityDtoList(Accommodation.class, stateId, accommodationCategoryId);
        return new ResponseEntity<List<MainCityDto>>(mainCityDtoList, HttpStatus.OK);
    }

    @GetMapping("/search/store/state")
    public ResponseEntity<?> findStoreStateCount(@RequestParam List<Long> smallCategoryId) {
        List<MainStateDto> mainStateDtoList = stateCityService.getMainStateDtoList(Store.class, smallCategoryId);
        return new ResponseEntity<List<MainStateDto>>(mainStateDtoList, HttpStatus.OK);
    }

    @GetMapping("/search/accommodation/state")
    public ResponseEntity<?> findAccommodationStateCount(@RequestParam List<Long> accommodationCategoryId) {
        List<MainStateDto> mainStateDtoList = stateCityService.getMainStateDtoList(Accommodation.class, accommodationCategoryId);
        return new ResponseEntity<List<MainStateDto>>(mainStateDtoList, HttpStatus.OK);
    }
}
