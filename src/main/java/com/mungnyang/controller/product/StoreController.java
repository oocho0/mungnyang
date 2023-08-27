package com.mungnyang.controller.product;

import com.mungnyang.dto.product.store.StoreTotalInfoDto;
import com.mungnyang.service.product.store.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @GetMapping("/admin/store")
    public String loadRegisterPage(Model model) {
        storeService.initializeStore(model);
        return "admin/register";
    }

    @PostMapping("/admin/store")
    public ResponseEntity<?> registerStore(@ModelAttribute @Valid StoreTotalInfoDto storeTotalInfoDto, BindingResult bindingResult) {
        return null;
    }
}
