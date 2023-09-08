package com.mungnyang.service.service;

import com.mungnyang.constant.IsTrue;
import com.mungnyang.dto.service.InitializeReservationRoomDto;
import com.mungnyang.dto.service.CalendarShowReservationRoomDto;
import com.mungnyang.dto.service.ModifyReservationRoomDto;
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
     *
     * @param room                             예약된 방
     * @param initializeReservationRoomDtoList 체크인 체크아웃 정보만 담긴 DTO
     */
    public void saveReservationRoom(Room room, List<InitializeReservationRoomDto> initializeReservationRoomDtoList) {
        for (InitializeReservationRoomDto initializeReservationRoomDto : initializeReservationRoomDtoList) {
            ReservationRoom createdReservationRoom = ReservationRoom.builder()
                    .room(room)
                    .checkInDate(initializeReservationRoomDto.getCheckInDate())
                    .checkOutDate(initializeReservationRoomDto.getCheckOutDate())
                    .build();
            reservationRoomRepository.save(createdReservationRoom);
        }
    }

    /**
     * RoomId로 해당 방의 수정 화면에 표시될 reservation-room 정보 리스트 찾기
     *
     * @param roomId 해당 방의 일련번호
     * @return ReservationRoomDto 리스트
     */
    public List<CalendarShowReservationRoomDto> getReservationRoomDtoListByRoomId(Long roomId, IsTrue isInitialRecord) {
        List<CalendarShowReservationRoomDto> reservationRoomDtoList = new ArrayList<>();
        List<ReservationRoom> reservationRoomList = new ArrayList<>();
        if (isInitialRecord == IsTrue.YES) {
            reservationRoomList = reservationRoomRepository.findByRoomRoomIdAndReservationNull(roomId);
            if (reservationRoomList.isEmpty()) {
                return null;
            }
            for (ReservationRoom reservationRoom : reservationRoomList) {
                reservationRoomDtoList.add(CalendarShowReservationRoomDto.builder()
                        .id(reservationRoom.getReservationRoomId())
                        .title("예약됨")
                        .start(reservationRoom.getCheckInDate())
                        .end(reservationRoom.getCheckOutDate())
                        .extendedProps(CalendarShowReservationRoomDto.ExtendedProps.builder()
                                .roomId(reservationRoom.getRoom().getRoomId())
                                .build())
                        .build());
            }
        } else {
            reservationRoomList = reservationRoomRepository.findByRoomRoomIdAndReservationNotNull(roomId);
            if (reservationRoomList.isEmpty()) {
                return null;
            }
            for (ReservationRoom reservationRoom : reservationRoomList) {
                reservationRoomDtoList.add(CalendarShowReservationRoomDto.builder()
                        .id(reservationRoom.getReservationRoomId())
                        .title(reservationRoom.getReservation().getMember().getName())
                        .start(reservationRoom.getCheckInDate())
                        .end(reservationRoom.getCheckOutDate())
                        .extendedProps(CalendarShowReservationRoomDto.ExtendedProps.builder()
                                .roomId(reservationRoom.getRoom().getRoomId())
                                .reservationId(reservationRoom.getReservation().getReservationId())
                                .reservationPrice(reservationRoom.getReservationPrice())
                                .build())
                        .build());
            }
        }
        return reservationRoomDtoList;
    }

    /**
     * 방 예약 정보 수정하기
     *
     * @param room            해당 방 일련번호
     * @param reservationList 화면에 표시된 예약 정보 리스트
     */
    public void updateReservationRoom(Room room, List<ModifyReservationRoomDto> reservationList) {
        for (ModifyReservationRoomDto modifyReservationRoomDto : reservationList) {
            if (modifyReservationRoomDto.getIsDelete().equals("Y")) {
                Long reservationRoomId = modifyReservationRoomDto.getReservationRoomId();
                ReservationRoom savedReservationRoom = getReservationRoomByReservationRoomId(reservationRoomId);
                reservationRoomRepository.delete(savedReservationRoom);
                continue;
            }
            if (modifyReservationRoomDto.getReservationRoomId() == null) {
                ReservationRoom newReservationRoom = ReservationRoom.builder()
                        .room(room)
                        .checkInDate(modifyReservationRoomDto.getCheckInDate())
                        .checkOutDate(modifyReservationRoomDto.getCheckOutDate())
                        .build();
                reservationRoomRepository.save(newReservationRoom);
            }
        }
    }


    /**
     * RoomId로 reservation-room 엔티티 리스트 찾기
     *
     * @param roomId 해당 방의 일련번호
     * @return ReservationRoom 엔티티 리스트
     */
    private List<ReservationRoom> getReservationRoomListByRoomId(Long roomId) {
        List<ReservationRoom> reservationRoomList = new ArrayList<>();
        reservationRoomList = reservationRoomRepository.findByRoomRoomIdOrderByReservationRoomId(roomId);
        return reservationRoomList;
    }

    private ReservationRoom getReservationRoomByReservationRoomId(Long reservationRoomId) {
        return reservationRoomRepository.findById(reservationRoomId).orElseThrow(IllegalArgumentException::new);
    }

}
