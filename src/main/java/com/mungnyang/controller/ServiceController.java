package com.mungnyang.controller;

import com.mungnyang.dto.product.CreateCommentDto;
import com.mungnyang.dto.service.*;
import com.mungnyang.entity.member.Member;
import com.mungnyang.entity.product.accommodation.Accommodation;
import com.mungnyang.entity.product.store.Store;
import com.mungnyang.entity.service.Cart;
import com.mungnyang.service.member.MemberService;
import com.mungnyang.service.product.accommodation.AccommodationCommentService;
import com.mungnyang.service.product.accommodation.AccommodationService;
import com.mungnyang.service.product.accommodation.room.RoomService;
import com.mungnyang.service.product.store.StoreCommentService;
import com.mungnyang.service.product.store.StoreService;
import com.mungnyang.service.service.CartRoomService;
import com.mungnyang.service.service.CartService;
import com.mungnyang.service.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class ServiceController {
    private final StoreService storeService;
    private final StoreCommentService storeCommentService;
    private final AccommodationService accommodationService;
    private final AccommodationCommentService accommodationCommentService;
    private final RoomService roomService;
    private final MemberService memberService;
    private final CartService cartService;
    private final ReservationService reservationService;
    private final CartRoomService cartRoomService;

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

    @GetMapping("/cart")
    public String loadCartPage(Principal principal, Model model) {
        Cart cart = memberService.getCart(principal.getName());
        model.addAttribute("cartId", cart.getCartId());
        List<CartRoomDto> cartRooms = cartService.getCartRoomListByMemberId(cart);
        model.addAttribute("cartRooms", cartRooms);
        return "user/cart";
    }

    @PostMapping("/cart/{accommodationId}/{roomId}")
    public ResponseEntity<?> savedCartRoom(@PathVariable Long accommodationId, @PathVariable Long roomId,
                                           @ModelAttribute ReservationInfoDto reservationInfoDto, Principal principal) {
        if (roomService.isNotOneOfAccommodationRoom(accommodationId, roomId)) {
            return new ResponseEntity<String>("잘못된 경로 입니다.", HttpStatus.BAD_REQUEST);
        }
        Member signInMember = memberService.getMemberByMemberEmail(principal.getName());
        cartService.addCartRoom(signInMember, roomId, reservationInfoDto);
        return new ResponseEntity<String>("success", HttpStatus.OK);
    }

    @DeleteMapping("/cart")
    public ResponseEntity<?> deleteSelectedCartRoom(@RequestParam List<Long> cartRoomId, Principal principal) {
        if (cartRoomService.isNotWrittenByPrinciple(cartRoomId, principal.getName())) {
            return new ResponseEntity<String>("삭제 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }
        cartRoomService.deleteCartRoomList(cartRoomId);
        return new ResponseEntity<String>("success", HttpStatus.OK);
    }

    @GetMapping("/reservations")
    public String loadReservationListPage(Principal principal, Model model) {
        List<ReservationDto> reservationDtoList = reservationService.getReservationList(principal.getName());
        model.addAttribute("reservations", reservationDtoList);
        return "user/reservationList";
    }

    @PostMapping("/reservation")
    public ResponseEntity<?> saveReservation(CreateReservationDto createReservationDto, Principal principal) {
        if (reservationService.isNotOneOfAccommodationRoom(createReservationDto.getReservationRoomList())) {
            return new ResponseEntity<String>("잘못된 경로 입니다.", HttpStatus.BAD_REQUEST);
        }
        if (reservationService.isDuplicate(createReservationDto)){
            return new ResponseEntity<String>("예약 건 중에 같은 숙소 호실에 기간이 중복되는 건이 존재합니다.", HttpStatus.BAD_REQUEST);
        }
        reservationService.saveReservation(principal.getName(), createReservationDto);
        return new ResponseEntity<String>("success", HttpStatus.OK);
    }

    @DeleteMapping("/reservation/{reservationId}")
    public ResponseEntity<?> deleteReservation(@PathVariable Long reservationId, Principal principal) {
        if (reservationService.isNotWrittenByPrinciple(reservationId, principal.getName())) {
            return new ResponseEntity<String>("삭제 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }
        reservationService.deleteReservation(reservationId);
        return new ResponseEntity<String>("success", HttpStatus.OK);
    }
}
