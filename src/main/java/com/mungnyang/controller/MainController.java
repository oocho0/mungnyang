package com.mungnyang.controller;

import com.mungnyang.dto.fixedEntityDto.MainBigCategoryDto;
import com.mungnyang.dto.fixedEntityDto.MainSmallCategoryDto;
import com.mungnyang.dto.fixedEntityDto.MainStateDto;
import com.mungnyang.dto.product.*;
import com.mungnyang.dto.product.accommodation.DetailAccommodationDto;
import com.mungnyang.dto.service.ReservationInfoDto;
import com.mungnyang.dto.product.accommodation.room.AvailableRoomDto;
import com.mungnyang.dto.product.store.DetailStoreDto;
import com.mungnyang.dto.service.CalendarShowReservationRoomDto;
import com.mungnyang.entity.product.accommodation.Accommodation;
import com.mungnyang.entity.product.store.Store;
import com.mungnyang.service.fixedEntity.CategoryService;
import com.mungnyang.service.fixedEntity.StateCityService;
import com.mungnyang.service.product.accommodation.AccommodationCommentService;
import com.mungnyang.service.product.accommodation.AccommodationService;
import com.mungnyang.service.product.accommodation.room.RoomService;
import com.mungnyang.service.product.store.StoreCommentService;
import com.mungnyang.service.product.store.StoreService;
import com.mungnyang.service.service.ReservationRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final CategoryService categoryService;
    private final StateCityService stateCityService;
    private final StoreService storeService;
    private final AccommodationService accommodationService;
    private final StoreCommentService storeCommentService;
    private final AccommodationCommentService accommodationCommentService;
    private final RoomService roomService;
    private final ReservationRoomService reservationRoomService;
    @GetMapping("/")
    public String getmainPage(Model model) {
        List<MainBigCategoryDto> bigCategoryDtoList = categoryService.getMainBigCategoryDtoForStore();
        model.addAttribute("bigCategories", bigCategoryDtoList);
        List<MainSmallCategoryDto> smallCategoryDtoList = categoryService.getMainSmallCategoryDtoByBigCategory(1L);
        model.addAttribute("smallCategories", smallCategoryDtoList);
        List<MainStateDto> storeStateDtoList = stateCityService.getMainStateDtoList(Store.class);
        model.addAttribute("storeState", storeStateDtoList);
        List<MainStateDto> accommodationStateList = stateCityService.getMainStateDtoList(Accommodation.class);
        model.addAttribute("accommodationState", accommodationStateList);
        List<MainTopDto> storeTopList = storeService.getStoresTopList();
        model.addAttribute("storeTopList", storeTopList);
        List<MainTopDto> accommodationTopList = accommodationService.getAccommodationsTopList();
        model.addAttribute("accommodationTopList", accommodationTopList);
        return "main";
    }
    @GetMapping("/search/store")
    public ResponseEntity<?> getStores(@ModelAttribute MultiParam param){
        List<ResultDto> resultDtoList = storeService.getStoreResultList(param);
        return new ResponseEntity<List<ResultDto>>(resultDtoList, HttpStatus.OK);
    }
    @GetMapping("/search/accommodation")
    public ResponseEntity<?> getAccommodations(@ModelAttribute SearchAccommodationFilter accommodationFilter) {
        List<ResultDto> resultDtoList = accommodationService.getAccommodationResultList(accommodationFilter);
        return new ResponseEntity<List<ResultDto>>(resultDtoList, HttpStatus.OK);
    }
    @GetMapping("/store/{storeId}")
    public String getStoreDetailPage(@PathVariable Long storeId, Model model, Principal principal){
        DetailStoreDto detailStoreDto = storeService.getStoreDetails(storeId);
        model.addAttribute("store", detailStoreDto);
        if (principal == null) {
            model.addAttribute("user", null);
        }else{
            model.addAttribute("user", principal.getName());
        }
        return "admin/detail";
    }

    @GetMapping("/accommodation/{accommodationId}")
    public String getAccommodationDetailPage(@PathVariable Long accommodationId, Model model, Principal principal,
                                             @ModelAttribute ReservationInfoDto reservationInfoDto) {
        if (reservationInfoDto.getHeadCount() == null) {
            LocalDateTime today = LocalDateTime.now().withHour(15).withMinute(0).withSecond(0).withNano(0);
            LocalDateTime tomorrow = LocalDateTime.now().withHour(11).withMinute(0).withSecond(0).withNano(0).plusDays(1);
            reservationInfoDto = new ReservationInfoDto(2, 1, today, tomorrow);
            model.addAttribute("reservationInfoDto", reservationInfoDto);
        }
        DetailAccommodationDto detailAccommodationDto = accommodationService.getAccommodationDetails(accommodationId, reservationInfoDto);
        model.addAttribute("accommodation", detailAccommodationDto);
        if (principal == null) {
            model.addAttribute("user", null);
        }else {
            model.addAttribute("user", principal.getName());
        }
        return "seller/detail";
    }

    @GetMapping("/accommodation/{accommodationId}/reservations")
    public ResponseEntity<?> getAvailableRoom(@PathVariable Long accommodationId,
                                              @ModelAttribute ReservationInfoDto reservationInfoDto){
        List<AvailableRoomDto> availableRoomDtoList = roomService.getRoomAvailable(accommodationId, reservationInfoDto);
        return new ResponseEntity<List<AvailableRoomDto>>(availableRoomDtoList, HttpStatus.OK);
    }

    @GetMapping("/store/{storeId}/comment")
    public ResponseEntity<?> getStoreCommentPaging(@PathVariable Long storeId,
                                                   @PageableDefault(size= 5, sort = "reqDate", direction = Sort.Direction.DESC) Pageable pageable, Principal principal) {
        Page<CommentDto> storeComments = storeCommentService.getCommentPage(storeId, pageable);
        String userEmail;
        if (principal == null) {
            userEmail = null;
        }else {
            userEmail = principal.getName();
        }
        CommentPagingDto commentPagingDto = new CommentPagingDto(storeComments, userEmail);
        if (commentPagingDto.getComments().getTotalElements() == 0L) {
            return new ResponseEntity<String>("none", HttpStatus.OK);
        }
        return new ResponseEntity<CommentPagingDto>(commentPagingDto, HttpStatus.OK);
    }

    @GetMapping("/accommodation/{accommodationId}/comment")
    public ResponseEntity<?> getAccommodationCommentPaging(@PathVariable Long accommodationId,
                                                           @PageableDefault(size= 5, sort = "reqDate", direction = Sort.Direction.DESC) Pageable pageable, Principal principal) {
        Page<CommentDto> accommodationComments = accommodationCommentService.getCommentPage(accommodationId, pageable);
        String userEmail;
        if (principal == null) {
            userEmail = null;
        }else{
            userEmail = principal.getName();
        }
        CommentPagingDto commentPagingDto = new CommentPagingDto(accommodationComments, userEmail);
        if (commentPagingDto.getComments().getTotalElements() == 0L) {
            return new ResponseEntity<String>("none", HttpStatus.OK);
        }
        return new ResponseEntity<CommentPagingDto>(commentPagingDto, HttpStatus.OK);
    }

    @GetMapping("/accommodation/{accommodationId}/{roomId}/reservations")
    public ResponseEntity<?> getReservationList(@PathVariable Long accommodationId, @PathVariable Long roomId) {
        if (roomService.isNotOneOfAccommodationRoom(accommodationId, roomId)) {
            return new ResponseEntity<String>("잘못된 경로 입니다.", HttpStatus.BAD_REQUEST);
        }
        List<CalendarShowReservationRoomDto> reservationRoomList = reservationRoomService.getReservationRoomDtoListByRoomIdForDetailPage(roomId);
        if (reservationRoomList == null) {
            return new ResponseEntity<String>("none", HttpStatus.OK);
        }
        return new ResponseEntity<List<CalendarShowReservationRoomDto>>(reservationRoomList, HttpStatus.OK);
    }
}
