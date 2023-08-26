package com.mungnyang.controller;

import com.mungnyang.entity.fixedEntity.SmallCategory;
import com.mungnyang.service.fixedEntity.CategoryService;
import com.mungnyang.service.fixedEntity.StateCityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final StateCityService stateCityService;
    private final CategoryService categoryService;

    @GetMapping("/")

    public String main() {
        return "main";
    }

    @GetMapping("/category/{bigCategoryId}")
    public ResponseEntity<?> findSmallCategoryIds(@PathVariable Long bigCategoryId) {
        List<SmallCategory> smallCategories = categoryService.getSmallCategoriesByBigCategoryId(bigCategoryId);
        return ResponseEntity.ok().body(smallCategories);
    }
}
