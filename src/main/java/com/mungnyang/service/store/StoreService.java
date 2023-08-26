package com.mungnyang.service.store;

import com.mungnyang.dto.store.StoreTotalInfoDto;
import com.mungnyang.entity.fixedEntity.BigCategory;
import com.mungnyang.entity.fixedEntity.City;
import com.mungnyang.entity.fixedEntity.SmallCategory;
import com.mungnyang.entity.fixedEntity.State;
import com.mungnyang.repository.store.StoreCommentRepository;
import com.mungnyang.repository.store.StoreImageRepository;
import com.mungnyang.repository.store.StoreRepository;
import com.mungnyang.service.fixedEntity.CategoryService;
import com.mungnyang.service.fixedEntity.StateCityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final StoreImageRepository storeImageRepository;
    private final StoreCommentRepository storeCommentRepository;
    private final StateCityService stateCityService;
    private final CategoryService categoryService;

    public void initializeStore(Model model) {
        StoreTotalInfoDto storeTotalInfoDto = new StoreTotalInfoDto();
        model.addAttribute("storeTotalInfoDto", storeTotalInfoDto);
        List<State> states = stateCityService.getAllStates();
        model.addAttribute("states", states);
        List<BigCategory> bigCategories = categoryService.getAllBigCategories();
        model.addAttribute("bigCategories", bigCategories);
    }

}
