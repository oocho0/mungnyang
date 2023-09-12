package com.mungnyang.controller;

import com.mungnyang.dto.product.CreateCommentDto;
import com.mungnyang.entity.product.accommodation.Accommodation;
import com.mungnyang.entity.product.store.Store;
import com.mungnyang.service.product.accommodation.AccommodationCommentService;
import com.mungnyang.service.product.accommodation.AccommodationService;
import com.mungnyang.service.product.store.StoreCommentService;
import com.mungnyang.service.product.store.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class ServiceController {
    private final StoreService storeService;
    private final StoreCommentService storeCommentService;
    private final AccommodationService accommodationService;
    private final AccommodationCommentService accommodationCommentService;
    @PostMapping("/store/{storeId}/comment")
    public ResponseEntity<?> saveStoreComment(@PathVariable Long storeId, CreateCommentDto createCommentDto) {
        Store savedStore = storeService.getStoreByStoreId(storeId);
        storeCommentService.saveStoreComment(savedStore, createCommentDto);
        return new ResponseEntity<String>("success", HttpStatus.OK);
    }

    @PostMapping("/accommodation/{accommodationId}/comment")
    public ResponseEntity<?> saveAccommodationComment(@PathVariable Long accommodationId, CreateCommentDto createCommentDto) {
        Accommodation savedAccommodation = accommodationService.getAccommodationByAccommodationId(accommodationId);
        accommodationCommentService.saveAccommodationComment(savedAccommodation, createCommentDto);
        return new ResponseEntity<String>("success", HttpStatus.OK);
    }

    @DeleteMapping("/store/{storeId}/comment/{storeCommentId}")
    public ResponseEntity<?> deleteStoreComment(@PathVariable Long storeId, @PathVariable Long storeCommentId, Principal principal) {
        if (storeCommentService.isNotWrittenByPrinciple(storeCommentId, principal.getName())) {
            return new ResponseEntity<String>("변경 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }
        if (storeCommentService.isNotOneOfStoreComment(storeId, storeCommentId)) {
            return new ResponseEntity<String>("잘못된 경로 입니다.", HttpStatus.BAD_REQUEST);
        }
        storeCommentService.deleteStoreComment(storeCommentId);
        return new ResponseEntity<String>("success", HttpStatus.OK);
    }

    @DeleteMapping("/accommodation/{accommodationId}/comment/{accommodationCommentId}")
    public ResponseEntity<?> deleteAccommodationComment(@PathVariable Long accommodationId, @PathVariable Long accommodationCommentId, Principal principal) {
        if (accommodationCommentService.isNotWrittenByPrinciple(accommodationCommentId, principal.getName())) {
            return new ResponseEntity<String>("변경 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }
        if (accommodationCommentService.isNotOneOfAccommodationComment(accommodationId, accommodationCommentId)) {
            return new ResponseEntity<String>("잘못된 경로 입니다.", HttpStatus.BAD_REQUEST);
        }
        accommodationCommentService.deleteAccommodationComment(accommodationCommentId);
        return new ResponseEntity<String>("success", HttpStatus.OK);
    }
}
