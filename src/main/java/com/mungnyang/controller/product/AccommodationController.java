package com.mungnyang.controller.product;

import com.mungnyang.dto.ErrorMessage;
import com.mungnyang.dto.product.accommodation.*;
import com.mungnyang.dto.product.accommodation.room.CreateRoomDto;
import com.mungnyang.dto.product.accommodation.room.RoomList;
import com.mungnyang.service.product.accommodation.AccommodationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/seller")
@RequiredArgsConstructor
@Slf4j
public class AccommodationController {

    private final AccommodationService accommodationService;

    @GetMapping("/accommodation")
    public String loadRegisterPage(Model model) {
        accommodationService.initializeCreateAccommodationPage(model);
        return "seller/register";
    }

    @PostMapping("/accommodation")
    public ResponseEntity<?> registerAccommodation(CreateAccommodationDto createAccommodationDto,
                                         @RequestPart("imageFile")
                                         List<MultipartFile> accommodationImageFileList,
                                         @ModelAttribute AccommodationFacilityList accommodationFacilityList,
                                         @ModelAttribute RoomList roomList) {
        List<FacilityDto> accommodationFacilities = accommodationFacilityList.getAccommodationFacilityList();
        List<CreateRoomDto> createRoomDtos = roomList.getRoomList();
        try {
            accommodationService.registerAccommodation(createAccommodationDto, accommodationImageFileList, accommodationFacilities, createRoomDtos);
        } catch (Exception e) {
            return new ResponseEntity<ErrorMessage>(new ErrorMessage(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<String>("success", HttpStatus.OK);
    }

    @GetMapping("/accommodations")
    public String loadListPage(Model model, Principal principal) {
        List<ListAccommodationDto> findAccommodations = accommodationService.getListAccommodationDtoListByCreatedBy(principal.getName());
        model.addAttribute("accommodations", findAccommodations);
        return "seller/list";
    }

    @GetMapping("/accommodations/{accommodationId}")
    public String loadModifyPage(@PathVariable Long accommodationId, Model model) {
        ModifyAccommodationDto findAccommodation = accommodationService.getModifyAccommodationDtoByAccommodationId(accommodationId);
        model.addAttribute(accommodationId);
        return "seller/modify";
    }
}
