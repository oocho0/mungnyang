package com.mungnyang.service.service;

import com.mungnyang.dto.product.accommodation.room.InitializeReservationRoomDto;
import com.mungnyang.dto.service.ReservationRoomDto;
import com.mungnyang.entity.product.accommodation.room.Room;
import com.mungnyang.entity.service.ReservationRoom;
import com.mungnyang.repository.service.ReservationRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReservationRoomService {

    private final ReservationRoomRepository reservationRoomRepository;

    /**
     * 숙소 관리자가 숙소 등록 페이지에 입력한 예약 현황 정보인 reservation-room 객체 저장
     * @param room 예약된 방
     * @param initializeReservationRoomDtoList 체크인 체크아웃 정보만 담긴 DTO
     */
    public void saveReservationRoom(Room room, List<InitializeReservationRoomDto> initializeReservationRoomDtoList){
        for (InitializeReservationRoomDto initializeReservationRoomDto : initializeReservationRoomDtoList) {
            ReservationRoom createdReservationRoom = ReservationRoom.builder()
                    .reservationRoomId(null)
                    .room(room)
                    .reservation(null)
                    .reservationPrice(null)
                    .checkInDate(initializeReservationRoomDto.getCheckInDate())
                    .checkOutDate(initializeReservationRoomDto.getCheckOutDate())
                    .build();
            reservationRoomRepository.save(createdReservationRoom);
        }
    }

    /**
     * RoomId로 해당 방의 화면에 표시될 reservation-room 정보 리스트 찾기
     * @param roomId 해당 방의 일련번호
     * @return ReservationRoomDto 리스트
     */
    public List<ReservationRoomDto> getReservationRoomDtoListByRoomId(Long roomId) {
        List<ReservationRoom> reservationRoomList = getReservationRoomListByRoomId(roomId);
        List<ReservationRoomDto> reservationRoomDtoList = new ArrayList<>();
        for (ReservationRoom reservationRoom : reservationRoomList) {
            reservationRoomDtoList.add(ReservationRoomDto.builder()
                    .reservationRoomId(reservationRoom.getReservationRoomId())
                    .roomId(reservationRoom.getRoom().getRoomId())
                    .reservationId(reservationRoom.getReservation().getReservationId())
                    .memberId(reservationRoom.getReservation().getMember().getMemberId())
                    .reservationPrice(reservationRoom.getReservationPrice())
                    .checkInDate(reservationRoom.getCheckInDate())
                    .checkOutDate(reservationRoom.getCheckOutDate())
                    .build());
        }
        return reservationRoomDtoList;
    }

    public void updateReservationRoom(Long roomId, List<ReservationRoomDto> reservationList) {

    }

    public void deleteReservationRoom(Long roomId) {

    }

    /**
     * RoomId로 reservation-room 엔티티 리스트 찾기
     * @param roomId 해당 방의 일련번호
     * @return ReservationRoom 엔티티 리스트
     */
    private List<ReservationRoom> getReservationRoomListByRoomId(Long roomId) {
        List<ReservationRoom> reservationRoomList = reservationRoomRepository.findByRoomRoomIdOrderByReservationRoomId(roomId);
        if (reservationRoomList == null) {
            throw new IllegalArgumentException();
        }
        return reservationRoomList;
    }

}
