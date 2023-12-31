package com.mungnyang.controller.product;

import com.mungnyang.constant.MemberType;
import com.mungnyang.constant.Role;
import com.mungnyang.constant.Url;
import com.mungnyang.dto.member.CreateMemberDto;
import com.mungnyang.dto.product.SearchStoreFilter;
import com.mungnyang.dto.product.store.CreateStoreDto;
import com.mungnyang.dto.product.store.ListStoreDto;
import com.mungnyang.dto.product.store.ModifyStoreDto;
import com.mungnyang.entity.fixedEntity.BigCategory;
import com.mungnyang.entity.fixedEntity.State;
import com.mungnyang.entity.member.Member;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;
    private final CategoryService categoryService;
    private final StateCityService stateCityService;
    private final MemberService memberService;

    @GetMapping("/store")
    public String loadRegisterPage(Model model) {
        List<BigCategory> bigCategories = categoryService.getBigCategoriesForStore();
        model.addAttribute("bigCategories", bigCategories);
        return "admin/register";
    }

    @PostMapping("/store")
    public ResponseEntity<String> registerStore(@ModelAttribute CreateStoreDto createStoreDto) {
        try {
            storeService.registerStore(createStoreDto);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @GetMapping("/stores")
    public String loadManagementPage(@ModelAttribute SearchStoreFilter searchStoreFilter,
                                     @PageableDefault(size= 10, sort = "storeId", direction = Sort.Direction.DESC) Pageable pageable,
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
        storeService.initializeModifyStorePage(model, modifyStoreDto);
        return "admin/modify";
    }

    @PutMapping("/stores/{storeId}")
    public ResponseEntity<String> updateStore(@PathVariable Long storeId, @ModelAttribute ModifyStoreDto modifyStoreDto) {
        try {
            storeService.updateStore(storeId, modifyStoreDto);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @DeleteMapping("/stores/{storeId}")
    public ResponseEntity<String> deleteStore(@PathVariable Long storeId) {
        try {
            storeService.deleteStore(storeId);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @GetMapping("/new")
    public String loadAddAdminPage(Model model) {
        CreateMemberDto createMemberDto = new CreateMemberDto();
        createMemberDto.setRole(Role.ADMIN.name());
        createMemberDto.setMemberType(MemberType.NORMAL.name());
        model.addAttribute("createMemberDto", createMemberDto);
        return "admin/addAdmin";
    }

    @PostMapping("/new")
    public String registerAdmin(@ModelAttribute @Valid CreateMemberDto createMemberDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "admin/addAdmin";
        }
        try {
            memberService.saveMember(createMemberDto);
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "admin/addAdmin";
        }
        return "redirect:" + Url.LOGIN;
    }
}
