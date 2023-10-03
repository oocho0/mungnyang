package com.mungnyang.service.service;

import com.mungnyang.constant.ReservationStatus;
import com.mungnyang.dto.service.CreateReservationDto;
import com.mungnyang.dto.service.CreateReservationRoomDto;
import com.mungnyang.dto.service.CreateReservationRoomDtoWithRoom;
import com.mungnyang.dto.service.ReservationDto;
import com.mungnyang.entity.member.Member;
import com.mungnyang.entity.service.Reservation;
import com.mungnyang.repository.service.ReservationRepository;
import com.mungnyang.service.member.MemberService;
import com.mungnyang.service.product.StatusService;
import com.mungnyang.service.product.accommodation.room.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationRoomService reservationRoomService;
    private final MemberService memberService;
    private final RoomService roomService;
    private final CartRoomService cartRoomService;

    /**
     * 회원의 예약 내역 리스트 반환
     *
     * @param email 해당 회원의 아이디이자 이메일
     * @return 예약 내역 정보 리스트
     */
    public List<ReservationDto> getReservationList(String email) {
        Member signInMember = getMemberByMemberEmail(email);
        List<ReservationDto> reservationDtoList = new ArrayList<>();
        List<Reservation> reservationList = getReservationListByMemberId(signInMember);
        for (Reservation reservation : reservationList) {
            reservationDtoList.add(ReservationDto.builder()
                    .reservationId(reservation.getReservationId())
                    .reservationStatus(StatusService.reservationStatusConverter(reservation.getReservationStatus()))
                    .reservationTotalPrice(reservation.getReservationTotalPrice())
                    .reservationDate(reservation.getReservationDate())
                    .reservationRoomList(reservationRoomService.getReservationRoomList(reservation.getReservationId()))
                    .build());
        }
        return reservationDtoList;
    }

    /**
     * 신규 예약 저장하기
     *
     * @param email                회원의 아이디이자 이메일
     * @param createReservationDto 예약 정보가 담긴 DTO
     */
    public void saveReservation(String email, CreateReservationDto createReservationDto) {
        Member signInMember = getMemberByMemberEmail(email);
        Reservation savedReservation = reservationRepository.save(Reservation.builder()
                .member(signInMember)
                .reservationDate(createReservationDto.getReservationDate())
                .reservationTotalPrice(createReservationDto.getReservationTotalPrice())
                .reservationStatus(ReservationStatus.RESERVATION)
                .build());
        List<CreateReservationRoomDtoWithRoom> createReservationRoomDtoWithRoomList = roomService.getCreateReservationRoomDtoWithRoom(createReservationDto.getReservationRoomList());
        List<Long> cartRoomId = reservationRoomService.saveReservationRoom(savedReservation, createReservationRoomDtoWithRoomList);
        if (cartRoomId.size() != 0) {
            cartRoomService.deleteCartRoomList(cartRoomId);
        }
    }

    public void deleteReservation(Long reservationId) {
        Reservation reservation = getReservationByReservationId(reservationId);
        reservationRoomService.deleteReservationRoom(reservationId);
        reservation.setReservationStatus(ReservationStatus.CANCEL);
    }

    /**
     * 회원 정보로 예약 내역 리스트 반환
     *
     * @param signInMember 해당 회원 정보
     * @return 예약 내역 Reservation 리스트
     */
    private List<Reservation> getReservationListByMemberId(Member signInMember) {
        return reservationRepository.findByMemberMemberId(signInMember.getMemberId());
    }

    public boolean isNotOneOfAccommodationRoom(List<CreateReservationRoomDto> createReservationRoomDtoList) {
        boolean result = false;
        for (CreateReservationRoomDto createReservationRoomDto : createReservationRoomDtoList) {
            Long accommodationId = createReservationRoomDto.getAccommodationId();
            Long roomId = createReservationRoomDto.getRoomId();
            result = roomService.isNotOneOfAccommodationRoom(accommodationId, roomId);
        }
        return result;
    }
    
    public boolean isDuplicate(CreateReservationDto createReservationDto) {
        List<CreateReservationRoomDto> createReservationRoomDtoList = createReservationDto.getReservationRoomList();
        for (int i = 0; i < createReservationRoomDtoList.size(); i++) {
            for (int j = i+1; j < createReservationRoomDtoList.size(); j++) {
                LocalDateTime stdCheckInDate = createReservationRoomDtoList.get(i).getCheckInDate();
                LocalDateTime stdCheckOutDate = createReservationRoomDtoList.get(i).getCheckOutDate();
                LocalDateTime comCheckInDate = createReservationRoomDtoList.get(j).getCheckInDate();
                LocalDateTime comCheckOutDate = createReservationRoomDtoList.get(j).getCheckOutDate();
                if (Objects.equals(createReservationRoomDtoList.get(i).getRoomId(), createReservationRoomDtoList.get(j).getRoomId())) {
                    if (stdCheckInDate.isEqual(comCheckInDate)) {
                        return true;
                    }
                    if (stdCheckOutDate.isEqual(comCheckOutDate)) {
                        return true;
                    }
                    if (stdCheckInDate.isAfter(comCheckInDate) && stdCheckInDate.isBefore(comCheckOutDate)) {
                        return true;
                    }
                    if (stdCheckOutDate.isAfter(comCheckInDate) && stdCheckOutDate.isBefore(comCheckOutDate)) {
                        return true;
                    }
                    if (comCheckInDate.isAfter(stdCheckInDate) && comCheckInDate.isBefore(stdCheckOutDate)) {
                        return true;
                    }
                    if (comCheckOutDate.isAfter(stdCheckInDate) && comCheckOutDate.isBefore(stdCheckOutDate)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isNotWrittenByPrinciple(Long reservationId, String email) {
        Reservation reservation = getReservationByReservationId(reservationId);
        return !Objects.equals(email, reservation.getCreatedBy());
    }

    private Reservation getReservationByReservationId(Long reservationId) {
        return reservationRepository.findById(reservationId).orElseThrow(IllegalArgumentException::new);
    }

    /**
     * 회원 아이디로 회원 정보 반환
     *
     * @param email 해당 회원의 아이디이자 이메일
     * @return Member 엔티티
     */
    private Member getMemberByMemberEmail(String email) {
        return memberService.getMemberByMemberEmail(email);
    }
}
