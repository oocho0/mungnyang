package com.mungnyang.controller.product;

import com.mungnyang.dto.ErrorMessage;
import com.mungnyang.dto.product.ModifyImageDto;
import com.mungnyang.dto.product.ModifyImageList;
import com.mungnyang.dto.product.SearchStoreFilter;
import com.mungnyang.dto.product.store.CreateStoreDto;
import com.mungnyang.dto.product.store.ListStoreDto;
import com.mungnyang.dto.product.store.ModifyStoreDto;
import com.mungnyang.dto.product.store.StoreImageDto;
import com.mungnyang.entity.fixedEntity.BigCategory;
import com.mungnyang.entity.fixedEntity.State;
import com.mungnyang.service.fixedEntity.CategoryService;
import com.mungnyang.service.fixedEntity.StateCityService;
import com.mungnyang.service.member.MemberService;
import com.mungnyang.service.product.store.StoreImageService;
import com.mungnyang.service.product.store.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;
    private final MemberService memberService;
    private final CategoryService categoryService;
    private final StateCityService stateCityService;
    private final StoreImageService storeImageService;

    @GetMapping("/store")
    public String loadRegisterPage(Model model) {
        List<BigCategory> bigCategories = categoryService.getBigCategoriesForStore();
        model.addAttribute("bigCategories", bigCategories);
        return "admin/register";
    }

    @PostMapping("/store")
    public ResponseEntity<?> registerStore(CreateStoreDto createStoreDto,
                                           @RequestPart("imageFile")
                                           List<MultipartFile> storeImageFileList) {
        try {
            storeService.registerStore(createStoreDto, storeImageFileList);
        } catch (Exception e) {
            return new ResponseEntity<ErrorMessage>(new ErrorMessage(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<String>("success", HttpStatus.OK);
    }

    @GetMapping("/stores")
    public String loadManagementPage(@ModelAttribute SearchStoreFilter searchStoreFilter,
                                     @PageableDefault(sort = "storeId", direction = Sort.Direction.DESC) Pageable pageable,
                                     Model model) {
        List<BigCategory> bigCategories = categoryService.getBigCategoriesForStore();
        model.addAttribute("bigCategories", bigCategories);
        List<State> states = stateCityService.getAllStates();
        model.addAttribute("states", states);
        Page<ListStoreDto> findStore = storeService.findStoresByConditionsAndPage(searchStoreFilter, pageable);
        model.addAttribute("stores", findStore);
        return "admin/list";
    }

    @GetMapping("/stores/{storeId}")
    public String loadModifyPage(@PathVariable Long storeId, Model model) {
        ModifyStoreDto modifyStoreDto = storeService.getModifyStoreDtoByStoreId(storeId);
        model.addAttribute("store", modifyStoreDto);
        List<StoreImageDto> storeImageDtoList = storeImageService.getStoreImageDtoListByStoreId(storeId);
        model.addAttribute("storeImages", storeImageDtoList);
        storeService.initializeModifyStorePage(model, modifyStoreDto);
        return "admin/modify";
    }

    @PutMapping("/stores/{storeId}")
    public ResponseEntity<?> updateStore(@PathVariable Long storeId,
                                         @ModelAttribute ModifyStoreDto modifyStoreDto,
                                         @ModelAttribute ModifyImageList imageList) {
        List<ModifyImageDto> modifyImageDtoList = imageList.getImageList();
        try {
            storeService.updateStore(storeId, modifyStoreDto, modifyImageDtoList);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<ErrorMessage>(new ErrorMessage(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<String>("success", HttpStatus.OK);
    }

    @DeleteMapping("/stores/{storeId}")
    public ResponseEntity<?> deleteStore(@PathVariable Long storeId) {
        try {
            storeService.deleteStore(storeId);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<ErrorMessage>(new ErrorMessage(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<String>("success", HttpStatus.OK);
    }
}
