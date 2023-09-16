package com.mungnyang.controller.product;

import com.mungnyang.constant.IsTrue;
import com.mungnyang.dto.product.accommodation.*;
import com.mungnyang.dto.product.accommodation.room.CreateRoomDto;
import com.mungnyang.dto.product.accommodation.room.InfoRoomDto;
import com.mungnyang.dto.product.accommodation.room.ModifyRoomDto;
import com.mungnyang.dto.service.CalendarShowReservationRoomDto;
import com.mungnyang.dto.service.InfoReservationRoomDto;
import com.mungnyang.entity.fixedEntity.SmallCategory;
import com.mungnyang.entity.product.accommodation.Accommodation;
import com.mungnyang.service.fixedEntity.CategoryService;
import com.mungnyang.service.product.accommodation.AccommodationService;
import com.mungnyang.service.product.accommodation.room.RoomService;
import com.mungnyang.service.service.ReservationRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/seller")
@RequiredArgsConstructor
@Slf4j
public class AccommodationController {

    private final AccommodationService accommodationService;
    private final CategoryService categoryService;
    private final RoomService roomService;
    private final ReservationRoomService reservationRoomService;

    @GetMapping("/accommodation")
    public String loadRegisterPage(Model model) {
        List<SmallCategory> smallCategoryList = categoryService.getSmallCategoryListByBigCategoryId(1L);
        model.addAttribute("smallCategories", smallCategoryList);
        return "seller/register";
    }

    @PostMapping("/accommodation")
    public ResponseEntity<String> registerAccommodation(@ModelAttribute CreateAccommodationDto createAccommodationDto) {
        try {
            accommodationService.registerAccommodation(createAccommodationDto);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @GetMapping("/accommodations")
    public String loadListPage(Model model, Principal principal) {
        List<ListAccommodationDto> findAccommodations = accommodationService.getListAccommodationDtoListByCreatedBy(principal.getName());
        model.addAttribute("accommodations", findAccommodations);
        return "seller/list";
    }

    @GetMapping("/accommodations/reservations")
    public String loadReservationListPageForSeller(Model model, Principal principal) {
        List<ReservationListAccommodationDto> accommodations = accommodationService.getReservationListAccommodationDtoList(principal.getName());
        model.addAttribute("accommodations", accommodations);
        return "seller/reservationList";
    }

    @GetMapping("/accommodations/reservations/{accommodationId}/{roomId}")
    public String loadReservationRoomPageForSeller(@PathVariable Long accommodationId, @PathVariable Long roomId,
                                                   Principal principal, Model model) {
        if (accommodationService.isNotWrittenByPrinciple(accommodationId, principal.getName())) {
            return "error";
        }
        if (roomService.isNotOneOfAccommodationRoom(accommodationId, roomId)) {
            return "error";
        }
        InfoRoomDto room = roomService.getInfoRoomDto(roomId);
        model.addAttribute("room", room);
        return "seller/reservationRoom";
    }

    @GetMapping("/accommodations/reservations/{roomId}/current")
    public ResponseEntity<?> getReservationRoomCurrentPaging(@PathVariable Long roomId,
                                                      @PageableDefault(size = 10, sort = "checkOutDate", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<InfoReservationRoomDto> infoReservationRoomDtoPage = reservationRoomService.getCurrentReservationRoomList(roomId, pageable);
        return new ResponseEntity<Page<InfoReservationRoomDto>>(infoReservationRoomDtoPage, HttpStatus.OK);
    }

    @GetMapping("/accommodations/reservations/{roomId}/past")
    public ResponseEntity<?> getReservationRoomPastPaging(@PathVariable Long roomId,
                                                      @PageableDefault(size = 10, sort = "checkOutDate", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<InfoReservationRoomDto> infoReservationRoomDtoPage = reservationRoomService.getPastReservationRoomList(roomId, pageable);
        return new ResponseEntity<Page<InfoReservationRoomDto>>(infoReservationRoomDtoPage, HttpStatus.OK);
    }

    @GetMapping("/accommodations/reservations/{roomId}/calendar")
    public ResponseEntity<?> loadReservationRoomListForReservationRoomPage(@PathVariable Long roomId) {
        List<CalendarShowReservationRoomDto> calendarList = reservationRoomService.getReservationRoomListForSeller(roomId);
        if (calendarList == null) {
            return new ResponseEntity<String>("none", HttpStatus.OK);
        }
        return new ResponseEntity<List<CalendarShowReservationRoomDto>>(calendarList, HttpStatus.OK);
    }

    @GetMapping("/accommodations/{accommodationId}")
    public String loadModifyPage(@PathVariable Long accommodationId, Model model, Principal principal) {
        if (accommodationService.isNotWrittenByPrinciple(accommodationId, principal.getName())) {
            return "error";
        }
        ModifyAccommodationDto findAccommodation = accommodationService.getModifyAccommodationDtoByAccommodationId(accommodationId);
        model.addAttribute("accommodation", findAccommodation);
        List<SmallCategory> smallCategoryList = categoryService.getSmallCategoryListByBigCategoryId(1L);
        model.addAttribute("smallCategories", smallCategoryList);
        return "seller/modify";
    }

    @PutMapping("/accommodations/{accommodationId}")
    public ResponseEntity<String> modifyAccommodation(@PathVariable Long accommodationId,
                                                      @ModelAttribute ModifyAccommodationDto modifyAccommodationDto,
                                                      Principal principal) {
        if (accommodationService.isNotWrittenByPrinciple(accommodationId, principal.getName())) {
            return new ResponseEntity<>("변경 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }
        try {
            accommodationService.updateAccommodation(accommodationId, modifyAccommodationDto);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @DeleteMapping("/accommodations/{accommodationId}")
    public ResponseEntity<String> deleteAccommodation(@PathVariable Long accommodationId,
                                                      Principal principal) {
        if (accommodationService.isNotWrittenByPrinciple(accommodationId, principal.getName())) {
            return new ResponseEntity<>("삭제 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }
        try {
            accommodationService.deleteAccommodation(accommodationId);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @GetMapping("/accommodations/{accommodationId}/room")
    public String loadAddRoomPage(@PathVariable Long accommodationId, Model model, Principal principal) {
        if (accommodationService.isNotWrittenByPrinciple(accommodationId, principal.getName())) {
            return "error";
        }
        model.addAttribute("accommodationId", accommodationId);
        return "seller/addRoom";
    }

    @PostMapping("/accommodations/{accommodationId}/room")
    public ResponseEntity<String> addRoom(@PathVariable Long accommodationId, @ModelAttribute CreateRoomDto createRoomDto,
                                          Principal principal) {
        if (accommodationService.isNotWrittenByPrinciple(accommodationId, principal.getName())) {
            return new ResponseEntity<>("추가 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }
        try {
            Accommodation savedAccommodation = accommodationService.getAccommodationByAccommodationId(accommodationId);
            roomService.saveRoom(savedAccommodation, createRoomDto);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @GetMapping("/accommodations/{accommodationId}/{roomId}")
    public String loadModifyRoomPage(@PathVariable Long accommodationId, @PathVariable Long roomId, Model model, Principal principal) {
        if (accommodationService.isNotWrittenByPrinciple(accommodationId, principal.getName())) {
            return "error";
        }
        if(roomService.isNotOneOfAccommodationRoom(accommodationId, roomId)){
            return "error";
        }
        ModifyRoomDto modifyRoomDto = roomService.getModifyRoomDtoByRoomId(roomId);
        model.addAttribute("room", modifyRoomDto);
        return "seller/modifyRoom";
    }

    @GetMapping("/accommodations/{accommodationId}/{roomId}/{isInit}")
    public ResponseEntity<?> getReservationList(@PathVariable Long accommodationId, @PathVariable Long roomId,
                                                @PathVariable String isInit, Principal principal) {
        if (accommodationService.isNotWrittenByPrinciple(accommodationId, principal.getName())) {
            return new ResponseEntity<String>("변경 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }
        if(roomService.isNotOneOfAccommodationRoom(accommodationId, roomId)){
            return new ResponseEntity<String>("잘못된 경로 입니다.", HttpStatus.BAD_REQUEST);
        }
        IsTrue isInitialRecord = isInit.equals("init") ? IsTrue.YES : IsTrue.NO ;
        List<CalendarShowReservationRoomDto> reservationRoomList = reservationRoomService.getReservationRoomDtoListByRoomId(roomId, isInitialRecord);
        if (reservationRoomList == null) {
            return new ResponseEntity<String>("none", HttpStatus.OK);
        }
        return new ResponseEntity<List<CalendarShowReservationRoomDto>>(reservationRoomList, HttpStatus.OK);
    }

    @PutMapping("/accommodations/{accommodationId}/{roomId}")
    public ResponseEntity<String> modifyRoom(@PathVariable Long accommodationId, @PathVariable Long roomId,
                                             @ModelAttribute ModifyRoomDto modifyRoomDto, Principal principal) {
        if (accommodationService.isNotWrittenByPrinciple(accommodationId, principal.getName())) {
            return new ResponseEntity<>("변경 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }
        if (roomService.isNotOneOfAccommodationRoom(accommodationId, roomId)) {
            return new ResponseEntity<>("잘못된 경로 입니다.", HttpStatus.BAD_REQUEST);
        }
        try {
            roomService.updateRoom(roomId, modifyRoomDto);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @DeleteMapping("/accommodations/{accommodationId}/{roomId}")
    public ResponseEntity<String> deleteRoom(@PathVariable Long accommodationId, @PathVariable Long roomId, Principal principal) {
        if (accommodationService.isNotWrittenByPrinciple(accommodationId, principal.getName())) {
            return new ResponseEntity<>("삭제 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }
        if (roomService.isNotOneOfAccommodationRoom(accommodationId, roomId)) {
            return new ResponseEntity<>("잘못된 경로 입니다.", HttpStatus.BAD_REQUEST);
        }
        try {
            roomService.deleteRoom(roomId);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

}
