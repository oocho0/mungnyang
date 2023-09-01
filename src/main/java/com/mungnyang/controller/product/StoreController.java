package com.mungnyang.controller.product;

import com.mungnyang.dto.ErrorMessage;
import com.mungnyang.dto.product.store.CreateStoreDto;
import com.mungnyang.service.product.store.StoreService;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("/store")
    public String loadRegisterPage(Model model) {
        storeService.initializeStore(model);
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
}
