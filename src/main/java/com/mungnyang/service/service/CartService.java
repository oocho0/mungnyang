package com.mungnyang.service.service;

import com.mungnyang.dto.service.ReservationInfoDto;
import com.mungnyang.dto.service.CartRoomDto;
import com.mungnyang.entity.member.Member;
import com.mungnyang.entity.product.accommodation.room.Room;
import com.mungnyang.entity.service.Cart;
import com.mungnyang.repository.service.CartRepository;
import com.mungnyang.service.product.accommodation.room.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final RoomService roomService;
    private final CartRoomService cartRoomService;

    /**
     * 예약 바구니 페이지에 표시될 모든 예약바구니-방 리스트 반환
     * @param cart 해당 카트 객체
     * @return CarRoomDto 리스트
     */
    public List<CartRoomDto> getCartRoomListByMemberId(Cart cart) {
        return cartRoomService.getAllCartRooms(cart);
    }

    /**
     * 신규 예약바구니-방 저장
     * @param member 로그인된 회원 객체
     * @param roomId 해당 방 일련번호
     * @param reservationInfoDto 예약바구니-방 정보가 담긴 DTO
     */
    public void addCartRoom(Member member, Long roomId, ReservationInfoDto reservationInfoDto) {
        Room findRoom = roomService.getRoomByRoomId(roomId);
        Cart findCart = getCartByMemberId(member.getMemberId());
        cartRoomService.saveCartRoom(findRoom, findCart, reservationInfoDto);
    }
    /**
     * 로그인된 회원의 카트 가지고 오기
     * @param memberId 해당 회원 일련번호
     * @return 해당 회원의 카트
     */
    public Cart getCartByMemberId(Long memberId) {
        return cartRepository.findByMemberMemberId(memberId);
    }

    /**
     * 카트 생성 하기
     * @param member 해당 회원
     */
    public void saveCart(Member member) {
        Cart newCart = new Cart();
        newCart.setMember(member);
        cartRepository.save(newCart);
    }
}
