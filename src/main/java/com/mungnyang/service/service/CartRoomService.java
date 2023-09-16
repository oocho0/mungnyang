package com.mungnyang.service.service;

import com.mungnyang.dto.service.ReservationInfoDto;
import com.mungnyang.dto.service.CartRoomDto;
import com.mungnyang.dto.service.SelectedCartRoom;
import com.mungnyang.entity.product.accommodation.room.Room;
import com.mungnyang.entity.service.Cart;
import com.mungnyang.entity.service.CartRoom;
import com.mungnyang.repository.service.CartRoomRepository;
import com.mungnyang.service.product.accommodation.AccommodationImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CartRoomService {
    private final CartRoomRepository cartRoomRepository;
    private final ReservationRoomService reservationRoomService;
    private final AccommodationImageService accommodationImageService;
    /**
     * 예약 바구니 페이지에 표시될 모든 예약바구니-방 정보 반환
     * @param cart 해당 예약 바구니 일련번호
     * @return CartRoomDto 리스트
     */
    public List<CartRoomDto> getAllCartRooms(Cart cart) {
        List<CartRoom> cartRoomList = getCartRoomListByCartId(cart.getCartId());
        List<CartRoomDto> cartRoomDtoList = new ArrayList<>();
        for (CartRoom cartRoom : cartRoomList) {
            cartRoomDtoList.add(CartRoomDto.builder()
                    .cartRoomId(cartRoom.getCartRoomId())
                    .accommodationId(cartRoom.getRoom().getAccommodation().getAccommodationId())
                    .accommodationName(cartRoom.getRoom().getAccommodation().getAccommodationName())
                    .roomId(cartRoom.getRoom().getRoomId())
                    .roomName(cartRoom.getRoom().getRoomName())
                    .roomPrice(cartRoom.getRoom().getRoomPrice())
                    .headCount(cartRoom.getHeadCount())
                    .days((int) ChronoUnit.DAYS.between(cartRoom.getCheckInDate(), cartRoom.getCheckOutDate()) + 1)
                    .checkInDate(cartRoom.getCheckInDate())
                    .checkOutDate(cartRoom.getCheckOutDate())
                    .accommodationImage(accommodationImageService.getAccommodationImage(cartRoom.getRoom().getAccommodation().getAccommodationId()))
                    .alreadyBooked(reservationRoomService.getIsAlreadyBooked(cartRoom.getCartRoomId(), cartRoom.getCheckInDate(), cartRoom.getCheckOutDate()))
                    .build());
        }
        return cartRoomDtoList;
    }

    /**
     * 신규 예악바구니-방 저장
     * @param room 해당 방 엔티티
     * @param cart 해당 예약 바구니 엔티티
     * @param reservationInfoDto 저장될 예약바구니-방 정보가 담긴 DTO
     */
    public void saveCartRoom(Room room, Cart cart, ReservationInfoDto reservationInfoDto) {
        cartRoomRepository.save(CartRoom.builder()
                .room(room)
                .cart(cart)
                .headCount(reservationInfoDto.getHeadCount())
                .checkInDate(reservationInfoDto.getCheckInDate())
                .checkOutDate(reservationInfoDto.getCheckOutDate())
                .build());
    }


    public void deleteCartRoomList(List<Long> cartRoomIdList) {
        for (Long cartRoomId : cartRoomIdList) {
            deleteCartRoom(cartRoomId);
        }
    }

    /**
     * 예약바구니-방 삭제
     * @param cartRoomId 해당 예약바구니-방 일련번호
     */
    public void deleteCartRoom(Long cartRoomId) {
        CartRoom savedCartRoom = getCartRoomByCartRoomId(cartRoomId);
        cartRoomRepository.delete(savedCartRoom);
    }


    /**
     * Url로 넘어온 cartRoomId와 로그인한 회원의 정보가 동일한지 판단해서 다르면 문제
     * @param cartRoomIds Url로 넘어온 cartRoomId 리스트
     * @param email 로그인한 회원의 아이디이자 이메일
     * @return 동일하지 않으면 true, 동일하면 문제없으므로 false
     */
    public boolean isNotWrittenByPrinciple(List<Long> cartRoomIds, String email) {
        boolean result = false;
        for (Long cartRoomId : cartRoomIds) {
            CartRoom savedCartRoom = getCartRoomByCartRoomId(cartRoomId);
            result = !Objects.equals(email, savedCartRoom.getCreatedBy());
        }
        return result;
    }

    /**
     * cartRoomId로 CartRoom 찾기
     * @param cartRoomId 해당 예약바구니-방 일련번호
     * @return CartRoom 엔티티
     */
    private CartRoom getCartRoomByCartRoomId(Long cartRoomId) {
        return cartRoomRepository.findById(cartRoomId).orElseThrow(IllegalArgumentException::new);
    }

    /**
     * cartId로 예약바구니의 모든 예약바구니-방 찾아 반환
     * @param cartId 해당 예약바구니 일련번호
     * @return CartRoom 엔티티 리스트
     */
    private List<CartRoom> getCartRoomListByCartId(Long cartId) {
        return cartRoomRepository.findByCartCartId(cartId);
    }
}
