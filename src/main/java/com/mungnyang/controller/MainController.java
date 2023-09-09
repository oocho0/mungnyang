package com.mungnyang.controller;

import com.mungnyang.dto.fixedEntityDto.MainBigCategoryDto;
import com.mungnyang.dto.fixedEntityDto.MainSmallCategoryDto;
import com.mungnyang.dto.fixedEntityDto.MainStateDto;
import com.mungnyang.entity.fixedEntity.SmallCategory;
import com.mungnyang.entity.product.accommodation.Accommodation;
import com.mungnyang.entity.product.store.Store;
import com.mungnyang.service.fixedEntity.CategoryService;
import com.mungnyang.service.fixedEntity.StateCityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final CategoryService categoryService;
    private final StateCityService stateCityService;

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
        return "main";
    }


}
